package com.example.currencyconverter.domain

data class Currency(
    val code: String
) {
    companion object {
        const val BASE_CURRENCY = "USD"
        const val TARGET_CURRENCY = "RUB"
    }
}
