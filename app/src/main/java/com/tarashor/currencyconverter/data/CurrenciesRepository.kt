package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import io.reactivex.Flowable

class CurrenciesRepository(val remoteDataSource: ICurrenciesDataSource) : ICurrenciesRepository{
    override fun getCurrencies(baseCurrency: String?): Flowable<CurrenciesDTO> {
        return remoteDataSource.getCurrencies(baseCurrency)
    }

}