package com.tarashor.currencyconverter.model

import android.arch.lifecycle.MutableLiveData
import com.tarashor.currencyconverter.data.CurrenciesDAO
import com.tarashor.currencyconverter.data.ICurrenciesRepository
import java.math.RoundingMode
import java.text.DecimalFormat

class CurrenciesInteractor(val repository: ICurrenciesRepository) {
    private val BASE_CURRENCY_ID = "EUR"

    val baseCurrency: Currency = Currency(BASE_CURRENCY_ID)

    fun reloadCurrencies(onLoaded: (Map<Currency, Double>) -> Unit){
        repository.isCacheDirty = true
        repository.getCurrencies(baseCurrency) {
            onLoaded(convertToModel(it))
        }
    }



    private fun convertToModel(dao: CurrenciesDAO?): Map<Currency, Double> {
        val items = mutableMapOf<Currency, Double>()
        if (dao != null){
            dao.rates.forEach{
                items.put(Currency(it.key), it.value)
            }
            items.put(baseCurrency, 1.0)
        }
        return items
    }
}

fun Double.toDecimalString():String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this)
}
