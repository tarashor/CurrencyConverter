package com.tarashor.currencyconverter.domain

import com.tarashor.currencyconverter.core.ISchedulerProvider
import com.tarashor.currencyconverter.data.ICurrenciesRepository
import com.tarashor.currencyconverter.entry.CurrenciesDTO
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit


class CurrenciesInteractor(private val repository: ICurrenciesRepository, private val schedulerProvider: ISchedulerProvider) : ICurrenciesInteractor {
    private val BASE_CURRENCY_ID = "EUR"
    private val BASE_CURRENCY_AMOUNT = 100.0

    override fun loadCurrencies(selectedCurrency: Observable<String>, enteredAmount: Observable<Double>): Flowable<CurrenciesAmount> {

        val inputFlowable = Observable.combineLatest(
            selectedCurrency.map { if (it.isBlank()) BASE_CURRENCY_ID else it },
            enteredAmount.map { if (it < 0) BASE_CURRENCY_AMOUNT else it },
            BiFunction<String, Double, Pair<String, Double>> { currency, amount ->
                Pair(currency, amount)
            }).toFlowable(BackpressureStrategy.LATEST)

        return Flowable.combineLatest(
            Flowable.interval(0, UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS, schedulerProvider.computation),
            inputFlowable,
            BiFunction<Long, Pair<String, Double>, Pair<String, Double>> { time, input -> input })
            .switchMap { input ->
                repository.getCurrencies(input.first)
                    .map { convertToCurrenciesAmount(it, input.second) }
            }
            .onErrorReturn {
                CurrenciesAmount(BASE_CURRENCY_ID, BASE_CURRENCY_AMOUNT, emptyMap())
            }
    }

    private fun convertToCurrenciesAmount(currencies: CurrenciesDTO, amount: Double) : CurrenciesAmount {
        val amounts = hashMapOf<String, Double>()
        for (rate in currencies.rates){
            amounts[rate.key] = convertAmountToOtherCurrency(amount, currencies.base, rate.key, currencies.rates)
        }
        return CurrenciesAmount(currencies.base, amount, amounts)
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

