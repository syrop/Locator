package pl.org.seva.locator.presentation

import kotlinx.coroutines.CoroutineScope
import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.usecase.DeleteTagUseCase
import pl.org.seva.locator.domain.usecase.GetAllTagsUseCase
import pl.org.seva.locator.domain.usecase.UpdateTagUseCase
import pl.org.seva.locator.presentation.architecture.BasePresentation
import pl.org.seva.locator.presentation.architecture.UseCaseExecutorProvider
import pl.org.seva.locator.presentation.mapper.TagDomainToPresentationMapper
import pl.org.seva.locator.presentation.mapper.TagPresentationToDomainMapper
import pl.org.seva.locator.presentation.model.CoordinatesViewState
import pl.org.seva.locator.presentation.model.TagPresentationModel

class CoordinatesPresentation(
    private val tagDomainToPresentationMapper: TagDomainToPresentationMapper,
    private val tagPresentationToDomainMapper: TagPresentationToDomainMapper,
    private val getAllTagsUseCase: GetAllTagsUseCase,
    private val updateTagUseCase: UpdateTagUseCase,
    private val deleteTagUseCase: DeleteTagUseCase,
    useCaseExecutorProvider: UseCaseExecutorProvider,
) : BasePresentation<CoordinatesViewState>(useCaseExecutorProvider) {

    override val initialViewState: CoordinatesViewState
        get() = CoordinatesViewState(emptyList())

    fun load(scope: CoroutineScope) {
        getAllTagsUseCase(scope, Unit, ::onLoaded)
    }

    fun onLoaded(list: List<Pair<TagDomainModel, ScanResultDomainModel>>) {
        updateViewState { CoordinatesViewState(list.map { tagDomainToPresentationMapper.toPresentation(it.first) }) }
    }

    fun onUpdated(list: List<TagDomainModel>) {
        updateViewState { CoordinatesViewState(list.map { tagDomainToPresentationMapper.toPresentation(it) }) }
    }

    fun update(scope: CoroutineScope, tag: TagPresentationModel) {
        updateTagUseCase(scope, tagPresentationToDomainMapper.toDomain(tag), ::onUpdated)
    }

    fun delete(scope: CoroutineScope, address: String) {
        deleteTagUseCase(scope, address, ::onUpdated)
    }

}
