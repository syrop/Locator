package pl.org.seva.locator.presentation.architecture

import pl.org.seva.locator.domain.cleanarchitecture.usecase.UseCaseExecutor

typealias UseCaseExecutorProvider =
    @JvmSuppressWildcards () -> UseCaseExecutor
