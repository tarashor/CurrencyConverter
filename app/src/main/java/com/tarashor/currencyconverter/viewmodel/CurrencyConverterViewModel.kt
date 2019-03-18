package com.tarashor.currencyconverter.viewmodel

import android.arch.lifecycle.*
import com.tarashor.currencyconverter.data.ICurrenciesRepository
import com.tarashor.currencyconverter.model.CurrenciesUIModel
import com.tarashor.currencyconverter.model.CurrenciesInteractor
import com.tarashor.currencyconverter.model.Currency
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class CurrencyConverterViewModel(repository: ICurrenciesRepository) : ViewModel() {
    val items = MutableLiveData<List<CurrencyViewModelItem>>()

    private val model = CurrenciesUIModel(repository)

    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private lateinit var scheduledFuture: ScheduledFuture<*>

    init {
        items.value = model.build()
    }

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
        }, 0, 60, TimeUnit.SECONDS)
    }

    fun stopPollingCurrencyRates() {
        scheduledFuture.cancel(true)
    }
}


class CurrencyViewModelItem(
    val currency: Currency,
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

    private val df: DecimalFormat = DecimalFormat("#.##")

    fun formattedAmount():String {
        df.roundingMode = RoundingMode.CEILING
        return df.format(amount)
    }

    fun parseAmount(str: String){
        amount = df.parse(str).toDouble()
    }
}
