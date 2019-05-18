package com.tarashor.currencyconverter.model

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import com.tarashor.currencyconverter.data.ICurrenciesRepository


class CurrenciesInteractor(val repository: ICurrenciesRepository) {
    private val BASE_CURRENCY_ID = "EUR"

    lateinit var baseCurrency: String
    val currenciesRates = hashMapOf<String, Double>()

    fun convertAmountToOtherCurrency(
        amount: Double,
        selectedCurrency: String?,
        currencyOut: String?
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

    fun reloadCurrencies(onLoaded: () -> Unit, newBaseCurrency: String? = null){
        repository.getCurrencies(newBaseCurrency) {
            setCurrenciesRates(it)
            onLoaded()
        }
    }

    private fun setCurrenciesRates(DTO: CurrenciesDTO?) {
        currenciesRates.clear()
        DTO?.rates?.forEach {
            currenciesRates[it.key] = it.value
        }
        DTO?.let {
            baseCurrency = it.base
            currenciesRates[it.base] = 1.0
        }
    }
}

