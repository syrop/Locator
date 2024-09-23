package pl.org.seva.locator.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.repository.TagRepository

class DeleteTagUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<String, List<TagDomainModel>>() {

    override suspend fun executeInBackground(request: String): List<TagDomainModel> {
        return withContext(Dispatchers.IO) {
            tagRepository.delete(request)
            tagRepository.load()
            tagRepository.tags.map { it.first }
        }
    }

}
