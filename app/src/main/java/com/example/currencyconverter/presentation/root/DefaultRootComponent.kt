package com.example.currencyconverter.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.currencyconverter.presentation.conversion.DefaultConversionComponent
import com.example.currencyconverter.presentation.result.DefaultResultComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val conversionComponentFactory: DefaultConversionComponent.Factory,
    private val resultComponentFactory: DefaultResultComponent.Factory,
    @Assisted componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Conversion,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            Config.Conversion -> {
                val component = conversionComponentFactory.create(
                    { amount, baseCode, targetCode ->
                        navigation.push(Config.Result(amount, baseCode, targetCode))
                    },
                    componentContext
                )
                RootComponent.Child.Conversion(component)
            }

            is Config.Result -> {
                val component = resultComponentFactory.create(
                    config.amount,
                    config.baseCode,
                    config.targetCode,
                    componentContext
                )
                RootComponent.Child.Result(component)
            }
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        data object Conversion : Config

        @Parcelize
        data class Result(
            val amount: Double,
            val baseCode: String,
            val targetCode: String
        ) : Config
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted componentContext: ComponentContext
        ): DefaultRootComponent
    }
}