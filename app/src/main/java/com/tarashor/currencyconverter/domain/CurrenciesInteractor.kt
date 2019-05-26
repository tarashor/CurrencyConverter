package com.tarashor.currencyconverter.domain

import com.tarashor.currencyconverter.data.ICurrenciesRepository
import com.tarashor.currencyconverter.entry.CurrenciesDTO
import io.reactivex.Observable
import io.reactivex.functions.BiFunction


class CurrenciesInteractor(val repository: ICurrenciesRepository) : ICurrenciesInteractor {
    private val BASE_CURRENCY_ID = "EUR"
    private val BASE_CURRENCY_AMOUNT = 100.0

    override fun loadCurrencies(selectedCurrency: Observable<String>, enteredAmount: Observable<Double>): Observable<CurrenciesAmount> {
        val ratesObservale = selectedCurrency
            .map {
                if (it.isBlank()) BASE_CURRENCY_ID else it
            }
            .flatMap {
                repository.getCurrencies(it).toObservable()
            }

        return Observable.combineLatest(
            ratesObservale,
            enteredAmount.map { if (it < 0) BASE_CURRENCY_AMOUNT else it },
            BiFunction<CurrenciesDTO, Double, CurrenciesAmount> { rates, amount ->
                val amounts = hashMapOf<String, Double>()
                for (rate in rates.rates){
                    amounts[rate.key] = convertAmountToOtherCurrency(amount, rates.base, rate.key, rates.rates)
                }
                CurrenciesAmount(rates.base, amount, amounts)
            })
            .onErrorReturn {
                CurrenciesAmount(BASE_CURRENCY_ID, BASE_CURRENCY_AMOUNT, emptyMap())
            }
    }




    private fun convertAmountToOtherCurrency(
        amount: Double,
        selectedCurrency: String?,
        currencyOut: String?,
        rates : Map<String, Double>
    ) : Double{
        return amount / (rates[selectedCurrency]?:1.0) * (rates[currencyOut]?:1.0)

    }

}

