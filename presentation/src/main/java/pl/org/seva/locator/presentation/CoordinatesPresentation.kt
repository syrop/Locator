package pl.org.seva.locator.presentation

import kotlinx.coroutines.CoroutineScope
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.usecase.GetAllTagsUseCase
import pl.org.seva.locator.presentation.architecture.BasePresentation
import pl.org.seva.locator.presentation.architecture.UseCaseExecutorProvider
import pl.org.seva.locator.presentation.mapper.TagDomainToPresentationMapper
import pl.org.seva.locator.presentation.model.CoordinatesViewState

class CoordinatesPresentation(
    private val tagDomainToPresentationMapper: TagDomainToPresentationMapper,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    useCaseExecutorProvider: UseCaseExecutorProvider,
) : BasePresentation<CoordinatesViewState>(useCaseExecutorProvider) {

    override val initialViewState: CoordinatesViewState
        get() = CoordinatesViewState(emptyList())

    fun load(scope: CoroutineScope) {
        getAllTagsUseCase(scope, Unit, ::onLoaded)
    }

    fun onLoaded(list: List<TagDomainModel>, scope: CoroutineScope) {
        updateViewState { CoordinatesViewState(list.map { tagDomainToPresentationMapper.toPresentation(it) }) }
    }

}
