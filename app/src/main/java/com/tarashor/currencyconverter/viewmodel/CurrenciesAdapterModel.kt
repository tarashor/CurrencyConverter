package com.tarashor.currencyconverter.viewmodel

import com.tarashor.currencyconverter.model.Currency

class CurrenciesAdapterModel(private var selectedCurrency: Currency,
                             private var enteredAmount: Double)
{

    private val history: MutableMap<Currency, Int> = hashMapOf()
    private val currencies: MutableMap<Currency, Double> = hashMapOf()

    fun setCurrencies(currencies: Map<Currency, Double>) {
        this.currencies.clear()
        currencies.forEach{
            this.currencies[it.key] = it.value
        }
    }

    fun setAmount(amount: Double) {
        enteredAmount = amount
    }

    fun setSelectedCurrency(currency: Currency, amount: Double) {
        history[selectedCurrency] = history.size
        selectedCurrency = currency
        enteredAmount = amount

    }

    fun convertAmountToOtherCurrency(
        amount: Double,
        currencyIn: Currency,
        currencyOut: Currency
    ) : Double
            = amount / (this.currencies[currencyIn]?:1.0) * ((this.currencies[currencyOut]?:1.0))

    fun build() : List<CurrencyViewModel> {
        val models = mutableListOf<CurrencyViewModel>()

        models.add(CurrencyViewModel(selectedCurrency, enteredAmount, true))

        history.filter { currencies.containsKey(it.key) }
            .map {
                CurrencyViewModel(
                    it.key,
                    convertAmountToOtherCurrency(enteredAmount, selectedCurrency, it.key),
                    false,
                    it.value
                )
            }.let { models.addAll(it) }

        currencies.filter { it.key != selectedCurrency && !history.contains(it.key) }
            .map {
                CurrencyViewModel(
                    it.key,
                    convertAmountToOtherCurrency(enteredAmount, selectedCurrency, it.key),
                    false
                )
            }
            .let { models.addAll(it) }

        return models.sorted()
    }
}