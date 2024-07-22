package com.example.currencyconverter.data

import com.example.currencyconverter.data.mapper.toEntities
import com.example.currencyconverter.data.network.api.ApiService
import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.domain.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : Repository {

    override suspend fun getCurrencyList(): List<Currency> {
        return apiService.getCurrencyList().toEntities()
    }

    override suspend fun getConversion(baseCode: String, targetCode: String): Double {
        return apiService.getConversion(baseCode, targetCode).conversion
    }
}