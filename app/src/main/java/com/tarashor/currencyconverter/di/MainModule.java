package com.tarashor.currencyconverter.di;

import com.tarashor.currencyconverter.data.*;
import com.tarashor.currencyconverter.domain.CurrenciesInteractor;
import com.tarashor.currencyconverter.domain.ICurrenciesInteractor;
import dagger.Module;
import dagger.Provides;


@Module(includes = RemoteModule.class)
public class MainModule {

//    @Provides
//    public CurrencyConverterViewModel viewModel(ICurrenciesInteractor interactor) {
//        return new CurrencyConverterViewModel(interactor);
//    }

    @Provides
    public ICurrenciesInteractor interactor(ICurrenciesRepository repository) {
        return new CurrenciesInteractor(repository);


    }

    @Provides
    public ICurrenciesRepository repository(ICurrenciesDataSource dataSource) {
        return new CurrenciesRepository(dataSource);
    }

    @Provides
    public ICurrenciesDataSource dataSource(APIRequest api) {
        return new CurrenciesRetrofitRemoteDataSource(api);
    }

}
