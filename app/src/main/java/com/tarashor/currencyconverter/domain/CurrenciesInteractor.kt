package com.tarashor.currencyconverter.domain

import com.tarashor.currencyconverter.data.ICurrenciesRepository
import com.tarashor.currencyconverter.entry.CurrenciesDTO
import com.tarashor.currencyconverter.ui.viewmodel.CurrencyConverterViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit


class CurrenciesInteractor(val repository: ICurrenciesRepository) : ICurrenciesInteractor {
    private val BASE_CURRENCY_ID = "EUR"
    private val BASE_CURRENCY_AMOUNT = 100.0

    override fun loadCurrencies(selectedCurrency: Observable<String>, enteredAmount: Observable<Double>): Flowable<CurrenciesAmount> {

        return Observable.combineLatest(
            selectedCurrency.map { if (it.isBlank()) BASE_CURRENCY_ID else it },
            enteredAmount.map { if (it < 0) BASE_CURRENCY_AMOUNT else it },
            BiFunction<String, Double, Pair<String, Double>> { currency, amount ->
                Pair(currency, amount)
            }).toFlowable(BackpressureStrategy.LATEST)
            .switchMap { input->
                Flowable.interval(UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS)
                    .switchMap { repository.getCurrencies(input.first) }
                    .map {
                        val amount = input.second
                        val amounts = hashMapOf<String, Double>()
                        for (rate in it.rates){
                            amounts[rate.key] = convertAmountToOtherCurrency(amount, it.base, rate.key, it.rates)
                        }
                        CurrenciesAmount(it.base, amount, amounts)
                    }
            }
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

    companion object {
        private val UPDATE_INTERVAL_SECONDS = 1L
    }

}

