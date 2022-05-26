package com.tarashor.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.app.Application
import com.tarashor.currencyconverter.di.AppComponent
import com.tarashor.currencyconverter.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class BaseApplication : Application(), HasAndroidInjector {

    private lateinit var appComponent: AppComponent

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>


    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
    }


    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
