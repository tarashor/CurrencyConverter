package com.tarashor.currencyconverter.domain

import com.tarashor.currencyconverter.di.ActivityScope
import com.tarashor.currencyconverter.ui.viewmodel.CurrencyViewModelItem
import javax.inject.Inject

@ActivityScope
class CurrenciesUIModel
@Inject
    constructor(private val interactor: ICurrenciesInteractor) {


    private var selectedCurrency: String? = null
    private var enteredAmount: Double = 100.0
    private val history: MutableMap<String, Long> = hashMapOf()

    private var currentHistoryIndex: Long = 0

    fun setAmount(amount: Double) {
        enteredAmount = amount
    }

    fun setSelectedCurrency(currency: String, amount: Double) {
        history.remove(currency)
        selectedCurrency?.run {
            history[this] = currentHistoryIndex
            currentHistoryIndex++
        }

        selectedCurrency = currency
        enteredAmount = amount
    }


    fun build() : List<CurrencyViewModelItem> {
        val models = mutableListOf<CurrencyViewModelItem>()

        selectedCurrency?.let {
            models.add(CurrencyViewModelItem(it, enteredAmount, true))
        }

        history.filter { interactor.currenciesRates.containsKey(it.key) }
            .map {
                CurrencyViewModelItem(
                    it.key,
                    interactor.convertAmountToOtherCurrency(enteredAmount, selectedCurrency, it.key),
                    false,
                    it.value
                )
            }.let { models.addAll(it) }

        interactor.currenciesRates.filter { it.key != selectedCurrency && !history.contains(it.key) }
            .map {
                CurrencyViewModelItem(
                    it.key,
                    interactor.convertAmountToOtherCurrency(enteredAmount, selectedCurrency, it.key),
                    false
                )
            }
            .let { models.addAll(it) }

        return models.sorted()
    }

    fun reloadRates(function: () -> Unit) {
        interactor.reloadCurrencies({
            if (selectedCurrency == null){
                selectedCurrency = interactor.baseCurrency
            }
            function()
        }, selectedCurrency)
    }
}