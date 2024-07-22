package com.example.currencyconverter.data.network.dto

import com.google.gson.annotations.SerializedName

data class ConversionDto(
    @SerializedName("base_code")
    val base: String,
    @SerializedName("target_code")
    val target: String,
    @SerializedName("conversion_rate")
    val conversion: Double
)