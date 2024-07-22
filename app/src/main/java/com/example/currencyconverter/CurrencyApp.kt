package com.example.currencyconverter

import android.app.Application
import com.example.currencyconverter.di.ApplicationComponent
import com.example.currencyconverter.di.DaggerApplicationComponent

class CurrencyApp: Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}