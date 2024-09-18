package pl.org.seva.locator.domain.usecase

import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.repository.TagRepository

class ScanUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<(List<Pair<TagDomainModel, ScanResultDomainModel>>) -> Unit, Unit>() {

    override suspend fun executeInBackground(request: (List<Pair<TagDomainModel, ScanResultDomainModel>>) -> Unit) {
        tagRepository.scan {
            request(tagRepository.tags)
        }
    }

}
