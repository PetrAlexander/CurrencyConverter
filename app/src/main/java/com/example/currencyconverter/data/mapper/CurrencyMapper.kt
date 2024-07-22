package com.example.currencyconverter.data.mapper

import com.example.currencyconverter.data.network.dto.CurrencyDto
import com.example.currencyconverter.domain.Currency

fun CurrencyDto.toEntities(): List<Currency> {
    val currencyList = mutableListOf<Currency>()
    codes.forEach {
        val currency = Currency(code = it[0])
        currencyList.add(currency)
    }
    return currencyList.toList()
}