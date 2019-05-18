package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO

interface ICurrenciesRepository {
    fun getCurrencies(baseCurrency: String?, callback: (CurrenciesDTO?) -> Unit)
}