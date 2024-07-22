package com.example.currencyconverter.data.network.api

import com.example.currencyconverter.data.network.dto.ConversionDto
import com.example.currencyconverter.data.network.dto.CurrencyDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("codes")
    suspend fun getCurrencyList(): CurrencyDto

    @GET("pair/{baseCode}/{targetCode}")
    suspend fun getConversion(
        @Path("baseCode") baseCode: String,
        @Path("targetCode") targetCode: String
    ): ConversionDto
}