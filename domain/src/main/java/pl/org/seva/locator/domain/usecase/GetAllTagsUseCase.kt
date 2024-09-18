package pl.org.seva.locator.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.repository.TagRepository

class GetAllTagsUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<Unit, List<Pair<TagDomainModel, ScanResultDomainModel>>>(){

    override suspend fun executeInBackground(request: Unit): List<Pair<TagDomainModel, ScanResultDomainModel>> {
        return withContext(Dispatchers.IO) {
            tagRepository.load()
            tagRepository.tags
        }
    }

}
