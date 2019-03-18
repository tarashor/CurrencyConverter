package com.tarashor.currencyconverter.model

import com.tarashor.currencyconverter.data.CurrenciesDAO
import com.tarashor.currencyconverter.data.ICurrenciesRepository


class CurrenciesInteractor(val repository: ICurrenciesRepository) {
    private val BASE_CURRENCY_ID = "EUR"

    lateinit var baseCurrency: Currency
    val currenciesRates = hashMapOf<Currency, Double>()

    fun convertAmountToOtherCurrency(
        amount: Double,
        selectedCurrency: Currency?,
        currencyOut: Currency?
    ) : Double{
        return if (this::baseCurrency.isInitialized){
            if (selectedCurrency == baseCurrency){
                amount  * (currenciesRates[currencyOut]?:1.0)
            } else {
                amount / (currenciesRates[selectedCurrency]?:1.0) * (this.currenciesRates[currencyOut]?:1.0)
            }
        } else {
            amount
        }
    }

    fun reloadCurrencies(onLoaded: () -> Unit, newBaseCurrency: Currency? = null){
        repository.isCacheDirty = true
        repository.getCurrencies(newBaseCurrency) {
            setCurrenciesRates(it)
            onLoaded()
        }
    }

    private fun setCurrenciesRates(dao: CurrenciesDAO?) {
        currenciesRates.clear()
        dao?.rates?.forEach {
            currenciesRates[Currency(it.key)] = it.value
        }
        dao?.let {
            baseCurrency = Currency(it.base)
            currenciesRates[baseCurrency] = 1.0
        }
    }
}

