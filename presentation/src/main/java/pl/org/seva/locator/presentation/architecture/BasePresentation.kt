package pl.org.seva.locator.presentation.architecture

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pl.org.seva.locator.domain.cleanarchitecture.exception.DomainException
import pl.org.seva.locator.domain.cleanarchitecture.usecase.UseCase

abstract class BasePresentation<VIEW_STATE : Any>(
    useCaseExecutorProvider: UseCaseExecutorProvider
) {

    protected abstract val initialViewState: VIEW_STATE

    private val mutableViewState by mutableStateFlow { initialViewState }
    val viewState by immutableFlow { mutableViewState  }

        private val useCaseExecutor by lazy {
        useCaseExecutorProvider()
    }

    private fun <INPUT, OUTPUT> execute(
        useCase: UseCase<INPUT, OUTPUT>,
        coroutineScope: CoroutineScope,
        value: INPUT,
        onSuccess: (OUTPUT, CoroutineScope) -> Unit = { _, _ -> },
        onException: (DomainException) -> Unit = {}
    ) {
        useCaseExecutor.execute(useCase, coroutineScope, value, onSuccess, onException)
    }

    protected operator fun <INPUT, OUTPUT> UseCase<INPUT, OUTPUT>.invoke(
        coroutineScope: CoroutineScope,
        value: INPUT,
        onSuccess: (OUTPUT, CoroutineScope) -> Unit = { _, _ -> },
        onException: (DomainException) -> Unit = {}
    ) {
        execute(this, coroutineScope, value, onSuccess, onException)
    }

    protected fun updateViewState(
        updatedState: VIEW_STATE.() -> VIEW_STATE
    ) = updateViewState(viewState.value.updatedState())

    private fun updateViewState(newViewState: VIEW_STATE) {
        mutableViewState.value = newViewState
    }

    private fun <T> mutableStateFlow(initialValueProvider: () -> T) =
        lazy { MutableStateFlow(initialValueProvider()) }

    private fun <T, FLOW : MutableStateFlow<T>> immutableFlow(
        initializer: () -> FLOW
    ): Lazy<StateFlow<T>> = lazy { initializer() }

}
