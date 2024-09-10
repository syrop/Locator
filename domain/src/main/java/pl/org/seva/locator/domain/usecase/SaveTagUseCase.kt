package pl.org.seva.locator.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.repository.TagRepository

class SaveTagUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<TagDomainModel, Unit>() {

    override suspend fun executeInBackground(request: TagDomainModel) {
        withContext(Dispatchers.IO) {
            tagRepository.save(request)
        }
    }

}
