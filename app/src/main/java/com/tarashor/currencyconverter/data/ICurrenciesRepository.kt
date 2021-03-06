package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import io.reactivex.Flowable

interface ICurrenciesRepository {
    fun getCurrencies(baseCurrency: String?): Flowable<CurrenciesDTO>
}