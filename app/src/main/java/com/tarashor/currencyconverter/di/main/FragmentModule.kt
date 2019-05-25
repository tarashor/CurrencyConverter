package com.tarashor.currencyconverter.di.main

import android.arch.lifecycle.ViewModel
import com.tarashor.currencyconverter.data.*
import com.tarashor.currencyconverter.di.ActivityScope
import com.tarashor.currencyconverter.di.ViewModelKey
import com.tarashor.currencyconverter.domain.CurrenciesInteractor
import com.tarashor.currencyconverter.domain.ICurrenciesInteractor
import com.tarashor.currencyconverter.ui.viewmodel.CurrencyConverterViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module
abstract class FragmentModule {

    @ActivityScope
    @Binds
    @IntoMap
    @ViewModelKey(CurrencyConverterViewModel::class)
    abstract fun bindReadingGoalWizardViewModel(viewModel: CurrencyConverterViewModel): ViewModel

    @Module
    companion object {

        @ActivityScope
        @JvmStatic
        @Provides
        fun provideDataSource(api:APIService) : ICurrenciesDataSource{
            return CurrenciesRetrofitRemoteDataSource(api)
        }

        @ActivityScope
        @JvmStatic
        @Provides
        fun provideRepository(dataSource :ICurrenciesDataSource) : ICurrenciesRepository{
            return CurrenciesRepository(dataSource)
        }

        @ActivityScope
        @JvmStatic
        @Provides
        fun provideInteractor(repository :ICurrenciesRepository) : ICurrenciesInteractor{
            return CurrenciesInteractor(repository)
        }
    }
}
