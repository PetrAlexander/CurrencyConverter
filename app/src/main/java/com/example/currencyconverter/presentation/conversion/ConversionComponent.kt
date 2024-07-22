package com.example.currencyconverter.presentation.conversion

import com.example.currencyconverter.domain.Currency
import kotlinx.coroutines.flow.StateFlow

interface ConversionComponent {
    val model: StateFlow<ConversionStore.State>

    fun changeAmountField(amount: String)

    fun clickConvert(amount: Double, baseCode: String, targetCode: String)

    fun changeCurrency(type: FieldType, currency: Currency)

    fun clickCurrency()
}