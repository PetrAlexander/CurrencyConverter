package com.example.currencyconverter.domain.usecases

import com.example.currencyconverter.domain.Currency
import com.example.currencyconverter.domain.Repository
import javax.inject.Inject

class GetConversionUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(base: Currency, target: Currency): Double {
        return repository.getConversion(base, target)
    }
}