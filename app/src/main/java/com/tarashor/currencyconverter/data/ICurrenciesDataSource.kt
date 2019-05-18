package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO

interface ICurrenciesDataSource {
    fun getCurrencies(baseCurrency: String?, callback: (CurrenciesDTO?) -> Unit)
}