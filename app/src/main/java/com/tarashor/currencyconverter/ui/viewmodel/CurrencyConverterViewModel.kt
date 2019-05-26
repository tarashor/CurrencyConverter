package com.tarashor.currencyconverter.ui.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.tarashor.currencyconverter.di.FragmentScope
import com.tarashor.currencyconverter.domain.CurrenciesAmount
import com.tarashor.currencyconverter.domain.ICurrenciesInteractor
import io.reactivex.BackpressureStrategy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FragmentScope
class CurrencyConverterViewModel
    @Inject
    constructor(private val interactor : ICurrenciesInteractor)
    : ViewModel() {
    private val disposable = CompositeDisposable()

    private val selectedCurrencyObservable = BehaviorSubject.createDefault("")
    private val amountObservable = BehaviorSubject.createDefault(-1.0)

//    private val selectedCurrencyObservable = PublishSubject.create<String>()
//    private val amountObservable = PublishSubject.create<Double>()


    private val history: MutableMap<String, Long> = hashMapOf()
    private var currentHistoryIndex: Long = 0

    val items : LiveData<List<CurrencyViewModelItem>>

    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private lateinit var scheduledFuture: ScheduledFuture<*>

    init{
        items = LiveDataReactiveStreams.fromPublisher(
            interactor.loadCurrencies(selectedCurrencyObservable, amountObservable)
                .toFlowable(BackpressureStrategy.LATEST)
                .map { build(it) }
                .subscribeOn(Schedulers.io()))



        disposable.add(selectedCurrencyObservable.skip(1)
            .zipWith(selectedCurrencyObservable,
            BiFunction<String, String, Pair<String, String>> { t1, t2 ->
                Pair(t1, t2)
            })
            .subscribe{
                history.remove(it.first)
                history[it.second] = currentHistoryIndex
                currentHistoryIndex++

            })



//        selectedCurrencyObservable.onNext("")
//        amountObservable.onNext(0.0)

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    fun setAmount(amount: Double) {
        amountObservable.onNext(amount)
    }

    fun setSelectedCurrency(currency: String, amount: Double) {
        selectedCurrencyObservable.onNext(currency)
        setAmount(amount)
    }

//    fun reloadRates(function: () -> Unit) {
//        interactor.reloadCurrencies({
//            if (selectedCurrency == null){
//                selectedCurrency = interactor.baseCurrency
//            }
//            function()
//        }, selectedCurrency)
//    }


    fun updateAmount(amount : Double){
        setAmount(amount)
    }

    fun updateSelectedCurrency(currency: CurrencyViewModelItem){
        setSelectedCurrency(currency.currency, currency.amount)
    }

    fun startPollingCurrencyRates() {
        scheduledFuture = scheduler.scheduleAtFixedRate(Runnable {
//            reloadRates{
//                notifyModelChanged()
//            }
        }, 0, UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS)
    }

    fun stopPollingCurrencyRates() {
        scheduledFuture.cancel(true)
    }

    private fun build(amouts: CurrenciesAmount) : List<CurrencyViewModelItem> {
        val models = mutableListOf<CurrencyViewModelItem>()

        models.add(CurrencyViewModelItem(amouts.baseCurrency, amouts.amount, true))

        history
            .filter { amouts.amounts.containsKey(it.key) }
            .map {
                val amount = amouts.amounts[it.key]
                if (amount == null) null
                else CurrencyViewModelItem(
                    it.key,
                    amount,
                    false,
                    it.value
                )}
            .forEach{ if(it != null) models.add(it)}

        amouts.amounts
            .filter { it.key != amouts.baseCurrency && !history.contains(it.key) }
            .map {
                CurrencyViewModelItem(
                    it.key,
                    it.value,
                    false
                )
            }
            .forEach{ models.add(it)}



        return models.sorted()
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
