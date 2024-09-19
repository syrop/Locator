package pl.org.seva.locator.presentation

import kotlinx.coroutines.CoroutineScope
import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.usecase.ContinuousScanUseCase
import pl.org.seva.locator.domain.usecase.GetAllTagsUseCase
import pl.org.seva.locator.domain.usecase.ScanUseCase
import pl.org.seva.locator.domain.usecase.StopScanUseCase
import pl.org.seva.locator.domain.usecase.UpdateTagUseCase
import pl.org.seva.locator.presentation.architecture.BasePresentation
import pl.org.seva.locator.presentation.architecture.UseCaseExecutorProvider
import pl.org.seva.locator.presentation.mapper.TagDomainToPresentationMapper
import pl.org.seva.locator.presentation.mapper.TagPresentationToDomainMapper
import pl.org.seva.locator.presentation.model.CoordinatesViewState
import pl.org.seva.locator.presentation.model.LocatorViewState
import pl.org.seva.locator.presentation.model.TagPresentationModel

class LocatorPresentation(
    private val tagDomainToPresentationMapper: TagDomainToPresentationMapper,
    private val tagPresentationToDomainMapper: TagPresentationToDomainMapper,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val continuousScanUseCase: ContinuousScanUseCase,
    private val stopScanUseCase: StopScanUseCase,
    useCaseExecutorProvider: UseCaseExecutorProvider,
) : BasePresentation<LocatorViewState>(useCaseExecutorProvider) {

    override val initialViewState: LocatorViewState
        get() = LocatorViewState(true, emptyList(), emptyMap())

    fun load(scope: CoroutineScope) {
        getAllTagsUseCase(scope, Unit, ::onLoadedWithScanResult)
    }

    fun onLoadedWithScanResult(list: List<Pair<TagDomainModel, ScanResultDomainModel>>) {
        updateViewState { withTags(list.map { tagDomainToPresentationMapper.toPresentation(it.first) }) }
    }

    fun scan(scope: CoroutineScope) {
        println("wiktor scanning")
        continuousScanUseCase(scope, ::onFound)
    }

    fun stopScan(scope: CoroutineScope) {
        stopScanUseCase(scope, Unit)
    }

    fun onFound(pair: Pair<TagDomainModel, ScanResultDomainModel>) {
        updateViewState { withMap(rssiMap + mapOf(pair.first.address to pair.second.rssi)) }
    }

}
