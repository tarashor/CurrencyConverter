package com.tarashor.currencyconverter.domain

import com.tarashor.currencyconverter.entry.CurrenciesDTO

interface ICurrenciesInteractor {
    var baseCurrency: String
    val currenciesRates : Map<String, Double>

    fun convertAmountToOtherCurrency(
        amount: Double,
        selectedCurrency: String?,
        currencyOut: String?
    ) : Double

    fun reloadCurrencies(onLoaded: () -> Unit, newBaseCurrency: String? = null)
    fun setCurrenciesRates(DTO: CurrenciesDTO?)
}