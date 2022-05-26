package com.tarashor.currencyconverter.ui.viewmodel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.tarashor.currencyconverter.di.FragmentScope
import com.tarashor.currencyconverter.domain.CurrenciesAmount
import com.tarashor.currencyconverter.domain.ICurrenciesInteractor
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@FragmentScope
class CurrencyConverterViewModel
    @Inject
    constructor(private val interactor : ICurrenciesInteractor)
    : ViewModel() {

    private val disposable = CompositeDisposable()

    private val selectedCurrencyObservable = BehaviorSubject.createDefault("")
    private val amountObservable = BehaviorSubject.createDefault(-1.0)

    private val history: MutableMap<String, Long> = hashMapOf()
    private var currentHistoryIndex: Long = 0

    val items : LiveData<List<CurrencyViewModelItem>>

    init{

        val responseObservable = interactor.loadCurrencies(selectedCurrencyObservable, amountObservable)
            .map { build(it) }
            .subscribeOn(Schedulers.io())

        items = LiveDataReactiveStreams.fromPublisher(responseObservable)

        disposable.add(
            Observable.zip(
                selectedCurrencyObservable.skip(1),
                selectedCurrencyObservable,
                BiFunction<String, String, Boolean> { t1, t2 ->
                    history.remove(t1)
                    history[t2] = currentHistoryIndex
                    currentHistoryIndex++
                    false
                }).subscribe())


    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }


    private fun setAmount(amount: Double) {
        amountObservable.onNext(amount)
    }

    private fun setSelectedCurrency(currency: String) {
        selectedCurrencyObservable.onNext(currency)
    }


    fun updateAmount(amount : Double){
        setAmount(amount)
    }

    fun updateSelectedCurrency(currency: CurrencyViewModelItem){
        setSelectedCurrency(currency.currency)
        setAmount(currency.amount)
    }

    private fun build(amounts: CurrenciesAmount) : List<CurrencyViewModelItem> {
        val models = mutableListOf<CurrencyViewModelItem>()

        models.add(CurrencyViewModelItem(amounts.baseCurrency, amounts.amount, true))

        history
            .filter { amounts.amounts.containsKey(it.key) }
            .map {
                val amount = amounts.amounts[it.key]
                if (amount == null) null
                else CurrencyViewModelItem(
                    it.key,
                    amount,
                    false,
                    it.value
                )}
            .forEach{ if(it != null) models.add(it)}

        amounts.amounts
            .filter { it.key != amounts.baseCurrency && !history.contains(it.key) }
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
