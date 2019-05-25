package com.tarashor.currencyconverter.di

import android.arch.lifecycle.ViewModelProvider
import com.tarashor.currencyconverter.URL_BASE
import com.tarashor.currencyconverter.core.ViewModelProviderFactory
import com.tarashor.currencyconverter.data.APIService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    @Module
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun provideConvertFactory() : Converter.Factory {
            return GsonConverterFactory.create()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideRetrofit(converterFactory: Converter.Factory) : Retrofit {

            return Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(converterFactory)
                .build()

        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideAPI(retrofit: Retrofit): APIService {

            return retrofit.create<APIService>(APIService::class.java)
        }
    }
}