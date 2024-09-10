package pl.org.seva.locator.domain.cleanarchitecture.usecase

import kotlinx.coroutines.CoroutineScope

abstract class BackgroundExecutingUseCase<REQUEST, RESULT> : UseCase<REQUEST, RESULT> {

    final override suspend fun execute(
        input: REQUEST,
        coroutineScope: CoroutineScope,
        onResult: (RESULT, CoroutineScope) -> Unit
    ) {
        val result = executeInBackground(input)
        onResult(result, coroutineScope)
    }

    abstract suspend fun executeInBackground(
        request: REQUEST
    ): RESULT
}
