package com.tarashor.currencyconverter.model

import android.arch.lifecycle.MutableLiveData
import com.tarashor.currencyconverter.data.CurrenciesDAO
import com.tarashor.currencyconverter.data.ICurrenciesRepository
import java.math.RoundingMode
import java.text.DecimalFormat

class Currencies(val repository: ICurrenciesRepository) {
    private val BASE_CURRENCY_ID = "EUR"

    val baseCurrency: Currency = Currency(BASE_CURRENCY_ID, 1.0, true)

    val currencies = MutableLiveData<List<Currency>>()

    fun reloadCurrencies(){
        repository.isCacheDirty = true
        repository.getCurrencies(baseCurrency) {
            currencies.value = convertToModel(it)
        }
    }

    private fun convertToModel(dao: CurrenciesDAO?): List<Currency> {
        val items = ArrayList<Currency>()
        if (dao != null){
            dao.rates.forEach{
                items.add(Currency(it.key, it.value))
            }
            items.add(baseCurrency)
        }
        return items.take(4)
    }

}

fun Double.toDecimalString():String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(this)
}
