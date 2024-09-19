package pl.org.seva.locator.domain.usecase

import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.model.ScanResultDomainModel
import pl.org.seva.locator.domain.model.TagDomainModel
import pl.org.seva.locator.domain.repository.TagRepository

class ContinuousScanUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<(Pair<TagDomainModel, ScanResultDomainModel>) -> Unit, Unit>() {

    override suspend fun executeInBackground(request: (Pair<TagDomainModel, ScanResultDomainModel>) -> Unit) {
        tagRepository.continuousScan {
            request(it)
        }
    }

}
