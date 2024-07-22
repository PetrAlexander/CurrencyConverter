package com.example.currencyconverter.data.network.dto

import com.google.gson.annotations.SerializedName

data class CurrencyDto(
    @SerializedName("supported_codes")
    val codes: List<List<String>>
)