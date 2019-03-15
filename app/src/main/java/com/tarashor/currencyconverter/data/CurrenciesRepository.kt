package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.model.Currency

interface ICurrenciesRepository : ICurrenciesDataSource{
    var isCacheDirty: Boolean
}

class CurrenciesRepository(val remoteDataSource: ICurrenciesDataSource) : ICurrenciesRepository{
    override fun getCurrencies(baseCurrency: Currency, callback: (CurrenciesDAO?) -> Unit) {
        if (isCacheDirty){
            remoteDataSource.getCurrencies(baseCurrency, callback)
        }
    }

    override var isCacheDirty: Boolean = true


}