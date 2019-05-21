package com.tarashor.currencyconverter.di;

import android.app.Application;
import com.tarashor.currencyconverter.BaseApplication;
import com.tarashor.currencyconverter.ui.MainActivity;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

import javax.inject.Singleton;

@Singleton
@Component(
        modules = {
//                AndroidSupportInjectionModule.class,
                ActivitiBuildersModule.class,
                AppModule.class
        })
public interface AppComponent {//extends AndroidInjector<BaseApplication> {
    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
     void inject(MainActivity activity);

//    Application get();
}
