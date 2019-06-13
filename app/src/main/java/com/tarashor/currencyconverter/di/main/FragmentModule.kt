package com.tarashor.currencyconverter.di.main

import android.arch.lifecycle.ViewModel
import com.tarashor.currencyconverter.core.SchedulerProvider
import com.tarashor.currencyconverter.data.*
import com.tarashor.currencyconverter.di.FragmentScope
import com.tarashor.currencyconverter.di.ViewModelKey
import com.tarashor.currencyconverter.domain.CurrenciesInteractor
import com.tarashor.currencyconverter.domain.ICurrenciesInteractor
import com.tarashor.currencyconverter.ui.viewmodel.CurrencyConverterViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class FragmentModule {

    @FragmentScope
    @Binds
    @IntoMap
    @ViewModelKey(CurrencyConverterViewModel::class)
    abstract fun bindCurrencyConverterViewModel(var1: CurrencyConverterViewModel): ViewModel

    @Module
    companion object {

        @JvmStatic
        @FragmentScope
        @Provides
        fun provideDataSource(api: APIService): ICurrenciesDataSource {
//            return CurrenciesRetrofitRemoteDataSource(api)
            return CurrenciesDummyDataSource()
        }

        @JvmStatic
        @FragmentScope
        @Provides
        fun provideRepository(dataSource: ICurrenciesDataSource): ICurrenciesRepository {
            return CurrenciesRepository(dataSource)
        }

        @JvmStatic
        @FragmentScope
        @Provides
        fun provideInteractor(repository: ICurrenciesRepository): ICurrenciesInteractor {
            return CurrenciesInteractor(repository, SchedulerProvider)
        }
    }


}
