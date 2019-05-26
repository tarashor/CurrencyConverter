package com.tarashor.currencyconverter.domain

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import io.reactivex.Flowable
import io.reactivex.Observable

interface ICurrenciesInteractor {

    fun loadCurrencies(
        selectedCurrency: Observable<String>, enteredAmount: Observable<Double>
    ) : Observable<CurrenciesAmount>

}