package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO

class CurrenciesRepository(val remoteDataSource: ICurrenciesDataSource) : ICurrenciesRepository{
    override fun getCurrencies(baseCurrency: String?, callback: (CurrenciesDTO?) -> Unit) {
        remoteDataSource.getCurrencies(baseCurrency, callback)
    }
}