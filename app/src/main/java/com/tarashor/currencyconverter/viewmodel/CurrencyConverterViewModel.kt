package com.tarashor.currencyconverter.viewmodel

import android.arch.lifecycle.*
import com.tarashor.currencyconverter.data.ICurrenciesRepository
import com.tarashor.currencyconverter.model.Currencies
import com.tarashor.currencyconverter.model.Currency
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class CurrencyConverterViewModel(repository: ICurrenciesRepository) : ViewModel() {
    val items = MediatorLiveData<CurrenciesAdapterModel>()

    private val model = Currencies(repository)

    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    init {
        items.value = CurrenciesAdapterModel(model.baseCurrency, 100.0, emptyList())

        items.addSource(model.currencies) {
            items.value?.values = it
            items.value = items.value
        }
    }

    fun updateAmount(amount : Double){
        //stopPollingCurrencyRates()
        items.value?.enteredAmount = amount
        items.value = items.value
        //startPollingCurrencyRates()
    }

    fun updateSelectedCurrency(currency: CurrencyViewModel){
        //stopPollingCurrencyRates()
        items.value?.selectedCurrency = currency.currency
        items.value?.enteredAmount = currency.amount
        items.value = items.value
        //startPollingCurrencyRates()
    }


    private lateinit var scheduledFuture: ScheduledFuture<*>

    fun startPollingCurrencyRates() {
        scheduledFuture = scheduler.scheduleAtFixedRate(Runnable {
            model.reloadCurrencies()
        }, 0, 30, TimeUnit.SECONDS)
    }

    fun stopPollingCurrencyRates() {
        scheduledFuture.cancel(true)
    }
}

class CurrenciesAdapterModel(
    var selectedCurrency:Currency,
    var enteredAmount: Double,
    var values: List<Currency>?
    ){
    fun build() : List<CurrencyViewModel> {
        val models = mutableListOf<CurrencyViewModel>()

        models.add(CurrencyViewModel(selectedCurrency, enteredAmount, true))

        val filteredViewModels = values?.filter { it != selectedCurrency }
            ?.map {
                CurrencyViewModel(
                    it, it.convertAmountToOtherCurrency(enteredAmount, selectedCurrency),
                    false
                )
            }?.sorted()

        if (filteredViewModels != null) models.addAll(filteredViewModels)

        return models
    }
}



class CurrencyViewModel(
    val currency: Currency,
    var amount: Double = 0.0,
    var isSelected: Boolean = false
) : Comparable<CurrencyViewModel> {
    override fun compareTo(other: CurrencyViewModel): Int = if (isSelected && !other.isSelected) 1
    else if (!isSelected && other.isSelected) -1
    else currency.compareTo(currency)

    override fun toString(): String {
        return "$currency - $amount"
    }
}
