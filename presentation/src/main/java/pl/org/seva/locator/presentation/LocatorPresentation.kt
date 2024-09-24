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
import sun.reflect.generics.scope.Scope
import kotlin.math.max
import kotlin.math.min

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
        val currentTime = System.currentTimeMillis()
        timeMap[pair.first.address] = currentTime
        updateViewState {
            val minX = tags.minOfOrNull { it.x } ?: 0
            val maxX = tags.maxOfOrNull { it.x } ?: 0
            val minY = tags.minOfOrNull { it.y } ?: 0
            val maxY = tags.maxOfOrNull { it.y } ?: 0
            val dX = maxX - minX
            val dY = maxY - minY
            val maxDistance = max(dX, dY)
            val mostRecentMap = rssiMap.filter {
                currentTime - (timeMap[it.key] ?: 0L) <= WINDOW
            }
            val distances = mutableListOf<Pair<String, Double>>()
            mostRecentMap.forEach {
                val normalizedRssi = min(max(it.value, -80), -40)
                distances.add(it.key to -(normalizedRssi + 40) / 40.0 * maxDistance)
            }
            if (distances.size >= 3) {
                locationUseCase(scope, distances, ::onLocation)
            }
            withMap(mostRecentMap + mapOf(pair.first.address to pair.second.rssi))
        }
    }

    fun onLocation(location: Pair<Double, Double>) {
        updateViewState { withLocation(location) }
    }

    companion object {
        const val WINDOW = 2000L
    }

}
