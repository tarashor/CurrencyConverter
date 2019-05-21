package com.tarashor.currencyconverter;

import android.app.Application;
import com.tarashor.currencyconverter.di.DaggerAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

//public class BaseApplication extends DaggerApplication {
//
//    @Override
//    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
//        return DaggerAppComponent.builder().application(this).build();
//    }
//}

public class BaseApplication extends Application {


}
