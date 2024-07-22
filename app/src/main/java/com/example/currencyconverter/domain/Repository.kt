package com.example.currencyconverter.domain

interface Repository {

    suspend fun getCurrencyList(): List<Currency>

    suspend fun getConversion(baseCode: String, targetCode: String): Double
}