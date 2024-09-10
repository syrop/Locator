package pl.org.seva.locator.domain.usecase

import pl.org.seva.locator.domain.cleanarchitecture.usecase.BackgroundExecutingUseCase
import pl.org.seva.locator.domain.repository.TagRepository

class StopScanUseCase(
    private val tagRepository: TagRepository,
) : BackgroundExecutingUseCase<Unit, Unit>() {

    override suspend fun executeInBackground(request: Unit) {
        tagRepository.stopScan()
    }

}
