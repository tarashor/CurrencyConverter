package com.tarashor.currencyconverter.di;

import com.tarashor.currencyconverter.domain.ICurrenciesInteractor;
import dagger.Component;

@Component(modules = {MainModule.class})
public interface CurrencyComponent {
    ICurrenciesInteractor getInteractor();
}
