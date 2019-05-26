package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import io.reactivex.Flowable

interface ICurrenciesDataSource {
    fun getCurrencies(baseCurrency: String?) : Flowable<CurrenciesDTO>
}