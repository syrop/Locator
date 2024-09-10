package pl.org.seva.locator.domain.usecase

import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.repository.TagRepository

class ScanUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<(List<TagDomainModel>) -> Unit, Unit>() {

    override suspend fun executeInBackground(request: (List<TagDomainModel>) -> Unit) {
        tagRepository.scan {
            request(tagRepository.tags)
        }
    }

}
