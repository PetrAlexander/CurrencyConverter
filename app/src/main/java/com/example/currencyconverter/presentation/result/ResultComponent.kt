package com.example.currencyconverter.presentation.result

import kotlinx.coroutines.flow.StateFlow

interface ResultComponent {
    val model: StateFlow<ResultStore.State>
}