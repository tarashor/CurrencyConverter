package com.tarashor.currencyconverter

import android.app.Activity
import android.app.Application
import com.tarashor.currencyconverter.di.AppComponent
import com.tarashor.currencyconverter.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class BaseApplication : Application(), HasActivityInjector {

    private lateinit var appComponent: AppComponent

    @Inject
    lateinit var activityInjector: AndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return activityInjector
    }
}
