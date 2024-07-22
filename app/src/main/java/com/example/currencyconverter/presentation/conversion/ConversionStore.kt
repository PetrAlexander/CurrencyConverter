package com.example.currencyconverter.presentation.conversion

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.domain.usecases.GetCurrencyListUseCase
import com.example.currencyconverter.presentation.conversion.ConversionStore.Intent
import com.example.currencyconverter.presentation.conversion.ConversionStore.Label
import com.example.currencyconverter.presentation.conversion.ConversionStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface ConversionStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeAmount(val amount: Double) : Intent

        data class ClickConvert(
            val amount: Double,
            val baseCode: String,
            val targetCode: String
        ) : Intent

        data class ChangeCurrency(
            val fieldType: FieldType,
            val currency: Currency
        ) : Intent

        data object ClickCurrency : Intent
    }

    data class State(
        val amount: Double,
        val baseCurrency: Currency,
        val targetCurrency: Currency,
        val currencyListState: CurrencyListState
    ) {
        sealed interface CurrencyListState {
            data object Initial : CurrencyListState

            data object Loading : CurrencyListState

            data object Error : CurrencyListState

            data class Loaded(val currencyList: List<Currency>) : CurrencyListState
        }
    }

    sealed interface Label {
        data class ClickConvert(
            val amount: Double,
            val baseCode: String,
            val targetCode: String
        ) : Label
    }
}

class ConversionStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getCurrencyListUseCase: GetCurrencyListUseCase
) {

    fun create(): ConversionStore =
        object : ConversionStore, Store<Intent, State, Label> by storeFactory.create(
            name = "ConversionStore",
            initialState = State(
                amount = 1.0,
                baseCurrency = Currency(Currency.BASE_CURRENCY),
                targetCurrency = Currency(Currency.TARGET_CURRENCY),
                currencyListState = State.CurrencyListState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeAmount(val amount: Double) : Msg

        data class ChangeCurrency(
            val fieldType: FieldType,
            val currency: Currency
        ) : Msg

        data class CurrencyListLoaded(val currencyList: List<Currency>) : Msg

        data object CurrencyListLoading : Msg

        data object CurrencyListLoadingError : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeAmount -> {
                    dispatch(Msg.ChangeAmount(intent.amount))
                }

                is Intent.ClickConvert -> {
                    with(intent) {
                        publish(Label.ClickConvert(amount, baseCode, targetCode))
                    }
                }

                is Intent.ChangeCurrency -> {
                    dispatch(Msg.ChangeCurrency(intent.fieldType, intent.currency))
                }

                Intent.ClickCurrency -> {
                    scope.launch {
                        dispatch(Msg.CurrencyListLoading)
                        try {
                            val currencyList = getCurrencyListUseCase()
                            dispatch(Msg.CurrencyListLoaded(currencyList))
                        } catch (e: Exception) {
                            dispatch(Msg.CurrencyListLoadingError)
                        }
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeAmount -> {
                copy(amount = msg.amount)
            }

            is Msg.ChangeCurrency -> {
                when (msg.fieldType) {
                    FieldType.BASE -> {
                        copy(baseCurrency = msg.currency)
                    }

                    FieldType.TARGET -> {
                        copy(targetCurrency = msg.currency)
                    }
                }
            }

            is Msg.CurrencyListLoaded -> {
                copy(currencyListState = State.CurrencyListState.Loaded(msg.currencyList))
            }

            Msg.CurrencyListLoading -> {
                copy(currencyListState = State.CurrencyListState.Loading)
            }

            Msg.CurrencyListLoadingError -> {
                copy(currencyListState = State.CurrencyListState.Error)
            }
        }
    }
}
