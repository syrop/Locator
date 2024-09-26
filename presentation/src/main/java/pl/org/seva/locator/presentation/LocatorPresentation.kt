package pl.org.seva.locator.presentation

import kotlinx.coroutines.CoroutineScope
import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.usecase.ContinuousScanUseCase
import pl.org.seva.locator.domain.usecase.GetAllTagsUseCase
import pl.org.seva.locator.domain.usecase.LocationUseCase
import pl.org.seva.locator.domain.usecase.StopScanUseCase
import pl.org.seva.locator.presentation.architecture.BasePresentation
import pl.org.seva.locator.presentation.architecture.UseCaseExecutorProvider
import pl.org.seva.locator.presentation.mapper.TagDomainToPresentationMapper
import pl.org.seva.locator.presentation.mapper.TagPresentationToDomainMapper
import pl.org.seva.locator.presentation.model.LocatorViewState
import kotlin.math.max
import kotlin.math.pow

class LocatorPresentation(
    private val tagDomainToPresentationMapper: TagDomainToPresentationMapper,
    private val tagPresentationToDomainMapper: TagPresentationToDomainMapper,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val continuousScanUseCase: ContinuousScanUseCase,
    private val stopScanUseCase: StopScanUseCase,
    private val locationUseCase: LocationUseCase,
    useCaseExecutorProvider: UseCaseExecutorProvider,
) : BasePresentation<LocatorViewState>(useCaseExecutorProvider) {

    private val timeMap = mutableMapOf<String, Long>()

    private val locations = mutableSetOf<Pair<Long, Pair<Double, Double>>>()

    override val initialViewState: LocatorViewState
        get() = LocatorViewState(true, emptyList(), emptyMap(), null)

    fun load(scope: CoroutineScope) {
        getAllTagsUseCase(scope, Unit, ::onLoadedWithScanResult)
    }

    fun onLoadedWithScanResult(list: List<Pair<TagDomainModel, ScanResultDomainModel>>) {
        updateViewState { withTags(list.map { tagDomainToPresentationMapper.toPresentation(it.first) }) }
    }

    fun startContinuousScanning(scope: CoroutineScope) {
        continuousScanUseCase(scope, { onFound(scope, it) })
    }

    fun stopScan(scope: CoroutineScope) {
        stopScanUseCase(scope, Unit)
    }

    fun onFound(scope: CoroutineScope, pair: Pair<TagDomainModel, ScanResultDomainModel>) {
        if (!viewState.value.tags.map { it.address }.contains(pair.first.address)){
            return
        }
        val currentTime = System.currentTimeMillis()
        timeMap[pair.first.address] = currentTime
        updateViewState {
            val minX = tags.minOfOrNull { it.x } ?: 0
            val maxX = tags.maxOfOrNull { it.x } ?: 0
            val minY = tags.minOfOrNull { it.y } ?: 0
            val maxY = tags.maxOfOrNull { it.y } ?: 0
            val mostRecentMap = rssiMap.filter {
                currentTime - (timeMap[it.key] ?: 0L) <= WINDOW
            }
            val distances = mutableMapOf<String, Double>()
            // https://stackoverflow.com/a/61986152/10821419
            mostRecentMap.keys.forEach { key ->
                val averageRssi = mostRecentMap.filter { it.key == key }.values.average()
                val distanceM = 10.0.pow((128.0 - averageRssi - 180.0) / (10 * 2))
                distances[key] = distanceM
            }
            if (distances.size >= 3) {
                locationUseCase(
                    scope,
                    distances.toList(),
                    { location ->
                        if (location.first < minX - 1.0 || location.first > maxX  + 1.0 ||
                            location.second < minY - 1.0 || location.second > maxY + 1.0) {
                            return@locationUseCase
                        }
                        onLocation(location)
                    }
                )
            }
            withMap(mostRecentMap + mapOf(pair.first.address to pair.second.rssi))
        }
    }

    fun onLocation(location: Pair<Double, Double>) {
        val now = System.currentTimeMillis()
        val mostRecentLocations = locations.filter {
            it.second.first.isFinite() && it.second.second.isFinite() && now - it.first <= WINDOW
        } + (now to location)
        locations.clear()
        locations.addAll(mostRecentLocations)
        val avgX = locations.map { it.second.first }.average()
        val avgY = locations.map { it.second.second }.average()
        updateViewState { withLocation(avgX to avgY) }
    }

    companion object {
        const val WINDOW = 10_000L
    }

}
