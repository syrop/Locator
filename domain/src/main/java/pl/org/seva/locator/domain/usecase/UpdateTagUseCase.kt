package pl.org.seva.locator.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.repository.TagRepository

class UpdateTagUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<TagDomainModel, List<TagDomainModel>>() {

    override suspend fun executeInBackground(request: TagDomainModel): List<TagDomainModel> {
        return withContext(Dispatchers.IO) {
            tagRepository.update(request)
            tagRepository.load()
            tagRepository.tags.map { it.first }
        }
    }

}
