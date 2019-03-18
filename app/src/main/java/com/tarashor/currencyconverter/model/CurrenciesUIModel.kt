package com.tarashor.currencyconverter.model

import com.tarashor.currencyconverter.data.ICurrenciesRepository
import com.tarashor.currencyconverter.viewmodel.CurrencyViewModelItem

class CurrenciesUIModel(repository: ICurrenciesRepository) {

    private val interactor = CurrenciesInteractor(repository)

    private var selectedCurrency: Currency? = null
    private var enteredAmount: Double = 100.0
    private val history: MutableMap<Currency, Long> = hashMapOf()

    private var currentHistoryIndex: Long = 0

    fun setAmount(amount: Double) {
        enteredAmount = amount
    }

    fun setSelectedCurrency(currency: Currency, amount: Double) {
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