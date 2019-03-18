package com.tarashor.currencyconverter.viewmodel

import android.arch.lifecycle.*
import com.tarashor.currencyconverter.data.ICurrenciesRepository
import com.tarashor.currencyconverter.model.CurrenciesInteractor
import com.tarashor.currencyconverter.model.Currency
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class CurrencyConverterViewModel(repository: ICurrenciesRepository) : ViewModel() {
    val items = MutableLiveData<List<CurrencyViewModel>>()

    private val interactor = CurrenciesInteractor(repository)
    private val model = CurrenciesAdapterModel(interactor.baseCurrency, 100.0)

    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private lateinit var scheduledFuture: ScheduledFuture<*>

    init {
        items.value = model.build()
    }

    private fun updateAvailableCurrencies(it: Map<Currency, Double>) {
        model.setCurrencies(it)
        items.value = model.build()
    }

    fun updateAmount(amount : Double){
        model.setAmount(amount)
        items.value = model.build()
    }

    fun updateSelectedCurrency(currency: CurrencyViewModel){
        model.setSelectedCurrency(currency.currency, currency.amount)
        items.value = model.build()
    }

    fun startPollingCurrencyRates() {
        scheduledFuture = scheduler.scheduleAtFixedRate(Runnable {
            interactor.reloadCurrencies(::updateAvailableCurrencies)
        }, 0, 1, TimeUnit.SECONDS)
    }

    fun stopPollingCurrencyRates() {
        scheduledFuture.cancel(true)
    }
}


class CurrencyViewModel(
    val currency: Currency,
    var amount: Double = 0.0,
    var isSelected: Boolean = false,
    var historyOrder: Int = -1
) : Comparable<CurrencyViewModel> {

    override fun compareTo(other: CurrencyViewModel): Int = if (isSelected && !other.isSelected) -1
    else if (!isSelected && other.isSelected) 1
    else if (historyOrder == -1 && other.historyOrder == -1) currency.compareTo(other.currency)
    else -historyOrder.compareTo(other.historyOrder)


    override fun toString(): String {
        return "$currency - $amount"
    }
}
