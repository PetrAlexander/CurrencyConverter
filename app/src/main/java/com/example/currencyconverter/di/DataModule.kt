package com.example.currencyconverter.di

import com.example.currencyconverter.data.RepositoryImpl
import com.example.currencyconverter.data.network.api.ApiFactory
import com.example.currencyconverter.data.network.api.ApiService
import com.example.currencyconverter.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindRepository(impl: RepositoryImpl): Repository

    companion object {
        @[ApplicationScope Provides]
        fun provideApiService(): ApiService = ApiFactory.apiService
    }
}