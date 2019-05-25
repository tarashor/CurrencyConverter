package com.tarashor.currencyconverter.di.main

import com.tarashor.currencyconverter.di.FragmentScope
import com.tarashor.currencyconverter.ui.view.CurrencyConverterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun fragment(): CurrencyConverterFragment

}