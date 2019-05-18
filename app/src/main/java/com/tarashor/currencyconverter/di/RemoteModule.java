package com.tarashor.currencyconverter.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tarashor.currencyconverter.ConstantsKt;
import com.tarashor.currencyconverter.data.APIRequest;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class RemoteModule {

    @Provides
    public APIRequest api(Retrofit retrofit) {
        return retrofit.create(APIRequest.class);
    }

    @Provides
    public Retrofit retrofit(GsonConverterFactory gsonConverterFactory) {
        return new Retrofit.Builder()
//                .client(okHttpClient)
                .baseUrl(ConstantsKt.getURL_BASE())
                .addConverterFactory(gsonConverterFactory)
                .build();


    }

    @Provides
    public Gson gson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

}
