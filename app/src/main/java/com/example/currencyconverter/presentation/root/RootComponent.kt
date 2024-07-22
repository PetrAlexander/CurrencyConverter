package com.example.currencyconverter.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.currencyconverter.presentation.conversion.ConversionComponent
import com.example.currencyconverter.presentation.result.ResultComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Conversion(val component: ConversionComponent): Child

        data class Result(val component: ResultComponent): Child
    }
}