package pl.org.seva.locator.presentation

import kotlinx.coroutines.CoroutineScope
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.usecase.ScanUseCase
import pl.org.seva.locator.domain.usecase.StopScanUseCase
import pl.org.seva.locator.presentation.architecture.BasePresentation
import pl.org.seva.locator.presentation.architecture.UseCaseExecutorProvider
import pl.org.seva.locator.presentation.mapper.TagDomainToPresentationMapper
import pl.org.seva.locator.presentation.model.ScannerViewState

class ScannerPresentation(
    private val tagDomainToPresentationMapper: TagDomainToPresentationMapper,
    private val scanUseCase: ScanUseCase,
    private val stopScanUseCase: StopScanUseCase,
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

}
