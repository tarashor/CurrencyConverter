package com.tarashor.currencyconverter.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    internal fun str(): String {
        return "Taras"
    }

    @Singleton
    @Provides
    internal fun b(app: Application): Any {
        return app == null
    }
}