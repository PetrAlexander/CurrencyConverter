package com.example.currencyconverter.domain.usecases

import com.example.currencyconverter.domain.Repository
import javax.inject.Inject

class GetCurrencyListUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke() = repository.getCurrencyList()
}