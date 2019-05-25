package com.tarashor.currencyconverter.di;

import com.tarashor.currencyconverter.di.main.MainActivityModule;
import com.tarashor.currencyconverter.ui.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Module
public abstract class ActivitiBuildersModule {


    @ActivityScope
    @ContributesAndroidInjector(modules = {MainActivityModule.class, AndroidSupportInjectionModule.class})
    abstract MainActivity contributeMainActivity();
}
