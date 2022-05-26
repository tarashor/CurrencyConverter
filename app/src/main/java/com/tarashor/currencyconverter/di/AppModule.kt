package com.tarashor.currencyconverter.di

import androidx.lifecycle.ViewModelProvider
import com.tarashor.currencyconverter.URL_BASE
import com.tarashor.currencyconverter.core.ViewModelProviderFactory
import com.tarashor.currencyconverter.data.APIService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory

    companion object {

        @Singleton
        @Provides
        fun provideConvertFactory() : Converter.Factory {
            return GsonConverterFactory.create()
        }

        @Singleton
        @Provides
        fun provideRetrofit(converterFactory: Converter.Factory) : Retrofit {

            return Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converterFactory)
                .build()

        }

        @Singleton
        @Provides
        fun provideAPI(retrofit: Retrofit): APIService {

            return retrofit.create(APIService::class.java)
        }
    }
}