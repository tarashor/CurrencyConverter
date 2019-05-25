package com.tarashor.currencyconverter.ui.viewmodel

import android.arch.lifecycle.*
import com.tarashor.currencyconverter.data.CurrenciesRepository
import com.tarashor.currencyconverter.di.FragmentScope
import com.tarashor.currencyconverter.di.ViewModelKey
import com.tarashor.currencyconverter.domain.CurrenciesUIModel
import com.tarashor.currencyconverter.domain.ICurrenciesInteractor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FragmentScope
class CurrencyConverterViewModel
    @Inject
    constructor(private val model : CurrenciesUIModel)
    : ViewModel() {

    val items = MutableLiveData<List<CurrencyViewModelItem>>()

    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private lateinit var scheduledFuture: ScheduledFuture<*>


    private fun notifyModelChanged(){
        items.value = model.build()
    }

    fun updateAmount(amount : Double){
        model.setAmount(amount)
        notifyModelChanged()
    }

    fun updateSelectedCurrency(currency: CurrencyViewModelItem){
        model.setSelectedCurrency(currency.currency, currency.amount)
        notifyModelChanged()
    }

    fun startPollingCurrencyRates() {
        scheduledFuture = scheduler.scheduleAtFixedRate(Runnable {
            model.reloadRates{
                notifyModelChanged()
            }
        }, 0, UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS)
    }

    fun stopPollingCurrencyRates() {
        scheduledFuture.cancel(true)
    }

    companion object {
        private val UPDATE_INTERVAL_SECONDS = 1L
    }
}


class CurrencyViewModelItem(
    val currency: String,
    var amount: Double = 0.0,
    var isSelected: Boolean = false,
    var historyOrder: Long = -1L
) : Comparable<CurrencyViewModelItem> {

    override fun compareTo(other: CurrencyViewModelItem): Int = if (isSelected && !other.isSelected) -1
    else if (!isSelected && other.isSelected) 1
    else if (historyOrder == -1L && other.historyOrder == -1L) currency.compareTo(other.currency)
    else -historyOrder.compareTo(other.historyOrder)


    override fun toString(): String {
        return "$currency - $amount"
    }

}
