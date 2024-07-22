package com.example.currencyconverter.domain.usecases

import com.example.currencyconverter.domain.Repository
import javax.inject.Inject

class GetConversionUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(baseCode: String, targetCode: String): Double {
        return repository.getConversion(baseCode, targetCode)
    }
}