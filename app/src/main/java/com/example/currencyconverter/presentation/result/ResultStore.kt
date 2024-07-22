package com.example.currencyconverter.presentation.result

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.currencyconverter.domain.usecases.GetConversionUseCase
import com.example.currencyconverter.presentation.result.ResultStore.Intent
import com.example.currencyconverter.presentation.result.ResultStore.Label
import com.example.currencyconverter.presentation.result.ResultStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ResultStore : Store<Intent, State, Label> {

    sealed interface Intent {
    }

    data class State(
        val amount: Double,
        val conversionState: ConversionState,
        val baseCode: String,
        val targetCode: String
    ) {
        sealed interface ConversionState {

            data object Initial : ConversionState

            data object Loading : ConversionState

            data object Error : ConversionState

            data class Loaded(val conversion: Double) : ConversionState
        }
    }

    sealed interface Label {
    }
}

class ResultStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getConversionUseCase: GetConversionUseCase,
) {

    fun create(
        amount: Double,
        baseCode: String,
        targetCode: String
    ): ResultStore =
        object : ResultStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ResultStore",
            initialState = State(
                amount,
                State.ConversionState.Initial,
                baseCode,
                targetCode
            ),
            bootstrapper = BootstrapperImpl(
                baseCode,
                targetCode,
                amount
            ),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class ConversionLoaded(val conversion: Double): Action

        data object ConversionStartLoading: Action

        data object ConversionLoadingError: Action
    }

    private sealed interface Msg {

        data class ConversionLoaded(val conversion: Double): Msg

        data object ConversionStartLoading: Msg

        data object ConversionLoadingError: Msg
    }

    private inner class BootstrapperImpl(
        private val baseCode: String,
        private val targetCode: String,
        private val amount: Double
        ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.ConversionStartLoading)
                try {
                    val conversion = getConversionUseCase(baseCode, targetCode)
                    val result = conversion * amount
                    dispatch(Action.ConversionLoaded(result))
                } catch (e: Exception) {
                    dispatch(Action.ConversionLoadingError)
                }
            }
        }
    }

    private class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.ConversionLoaded -> {
                    dispatch(Msg.ConversionLoaded(action.conversion))
                }
                Action.ConversionLoadingError -> {
                    dispatch(Msg.ConversionLoadingError)
                }
                Action.ConversionStartLoading -> {
                    dispatch(Msg.ConversionStartLoading)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ConversionLoaded -> {
                copy(conversionState = State.ConversionState.Loaded(msg.conversion))
            }
            Msg.ConversionLoadingError -> {
                copy(conversionState = State.ConversionState.Error)
            }
            Msg.ConversionStartLoading -> {
                copy(conversionState = State.ConversionState.Loading)
            }
        }
    }
}
