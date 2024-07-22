package com.example.currencyconverter.domain

interface Repository {

    suspend fun getCurrencyList(): List<Currency>

    suspend fun getConversion(base: Currency, target: Currency): Double
}