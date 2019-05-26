package com.tarashor.currencyconverter.domain

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import com.tarashor.currencyconverter.data.ICurrenciesRepository
import javax.inject.Inject


class CurrenciesInteractor3(val repository: ICurrenciesRepository) : ICurrenciesInteractor3 {
    override fun reloadCurrencies(onLoaded: () -> Unit, newBaseCurrency: String?) {

    }

    private val BASE_CURRENCY_ID = "EUR"

    override lateinit var baseCurrency: String
    override val currenciesRates = hashMapOf<String, Double>()

    override fun convertAmountToOtherCurrency(
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


    override fun setCurrenciesRates(DTO: CurrenciesDTO?) {
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

