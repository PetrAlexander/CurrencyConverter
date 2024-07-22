package com.example.currencyconverter.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.currencyconverter.presentation.conversion.ConversionContent
import com.example.currencyconverter.presentation.result.ResultContent
import com.example.currencyconverter.presentation.theme.CurrencyConverterTheme

@Composable
fun RootContent(component: RootComponent) {
    CurrencyConverterTheme {
        Children(
            stack = component.stack
        ) {
            when (val instance = it.instance) {
                is RootComponent.Child.Conversion -> {
                    ConversionContent(component = instance.component)
                }
                is RootComponent.Child.Result -> {
                    ResultContent(component = instance.component)
                }
            }
        }
    }
}