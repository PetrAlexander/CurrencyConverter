package com.example.currencyconverter.presentation.conversion

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultConversionComponent @AssistedInject constructor(
    private val conversionStoreFactory: ConversionStoreFactory,
    @Assisted private val onConvertClick: (Double, String, String) -> Unit,
    @Assisted componentContext: ComponentContext
) : ConversionComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { conversionStoreFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is ConversionStore.Label.ClickConvert -> {
                        onConvertClick(it.amount, it.baseCode, it.targetCode)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ConversionStore.State> = store.stateFlow

    override fun changeAmountField(amount: String) {
        store.accept(ConversionStore.Intent.ChangeAmount(amount))
    }

    override fun clickConvert(amount: Double, baseCode: String, targetCode: String) {
        store.accept(ConversionStore.Intent.ClickConvert(amount, baseCode, targetCode))
    }

    override fun changeCurrency(type: FieldType, currency: Currency) {
        store.accept(ConversionStore.Intent.ChangeCurrency(type, currency))
    }

    override fun clickCurrency() {
        store.accept(ConversionStore.Intent.ClickCurrency)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted onConvertClick: (Double, String, String) -> Unit,
            @Assisted componentContext: ComponentContext
        ): DefaultConversionComponent
    }
}