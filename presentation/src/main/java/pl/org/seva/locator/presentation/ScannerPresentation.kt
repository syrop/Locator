package pl.org.seva.locator.presentation

import kotlinx.coroutines.CoroutineScope
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.usecase.AddTagUseCase
import pl.org.seva.locator.domain.usecase.ScanUseCase
import pl.org.seva.locator.domain.usecase.StopScanUseCase
import pl.org.seva.locator.presentation.architecture.BasePresentation
import pl.org.seva.locator.presentation.architecture.UseCaseExecutorProvider
import pl.org.seva.locator.presentation.mapper.TagDomainToPresentationMapper
import pl.org.seva.locator.presentation.mapper.TagPresentationToDomainMapper
import pl.org.seva.locator.presentation.model.ScannerViewState
import pl.org.seva.locator.presentation.model.TagPresentationModel

class ScannerPresentation(
    private val tagDomainToPresentationMapper: TagDomainToPresentationMapper,
    private val tagPresentationToDomainMapper: TagPresentationToDomainMapper,
    private val scanUseCase: ScanUseCase,
    private val stopScanUseCase: StopScanUseCase,
    private val saveUseCase: AddTagUseCase,
    useCaseExecutorProvider: UseCaseExecutorProvider,
): BasePresentation<ScannerViewState>(useCaseExecutorProvider) {

    override val initialViewState: ScannerViewState
        get() = ScannerViewState(emptyList())

    fun scan(scope: CoroutineScope) {
        scanUseCase(scope, ::onFound)
    }

    fun stopScan(scope: CoroutineScope) {
        stopScanUseCase(scope, Unit)
    }

    fun onFound(list: List<TagDomainModel>) {
        updateViewState { ScannerViewState(list.map { tagDomainToPresentationMapper.toPresentation(it) }) }
    }

    fun save(scope: CoroutineScope, tag: TagPresentationModel) {
        saveUseCase(scope, tagPresentationToDomainMapper.toDomain(tag))
    }

}
