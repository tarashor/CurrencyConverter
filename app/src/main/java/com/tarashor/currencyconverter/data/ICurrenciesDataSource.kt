package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.model.Currency

interface ICurrenciesDataSource {
    fun getCurrencies(baseCurrency: Currency?, callback: (CurrenciesDAO?) -> Unit)
}