package com.example.currencyconverter.presentation.result

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultResultComponent @AssistedInject constructor(
    private val resultStoreFactory: ResultStoreFactory,
    @Assisted private val amount: Double,
    @Assisted("baseCode") private val baseCode: String,
    @Assisted("targetCode") private val targetCode: String,
    @Assisted componentContext: ComponentContext
) : ResultComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        resultStoreFactory.create(amount, baseCode, targetCode)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<ResultStore.State> = store.stateFlow

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted amount: Double,
            @Assisted("baseCode") baseCode: String,
            @Assisted("targetCode") targetCode: String,
            @Assisted componentContext: ComponentContext
        ): DefaultResultComponent
    }
}