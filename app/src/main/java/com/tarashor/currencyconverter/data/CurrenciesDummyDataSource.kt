package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import io.reactivex.Flowable
import java.util.*

class CurrenciesDummyDataSource : ICurrenciesDataSource {
    override fun getCurrencies(baseCurrency: String?): Flowable<CurrenciesDTO> {
        val dto = CurrenciesDTO(baseCurrency ?: "", noise())
        return Flowable.just(dto)
    }

    private val map: Map<String, Double> = mapOf(
        "UAH" to 29.423548,
        "RUB" to 72.288964,
        "USD" to 1.116345,
        "UGX" to 4194.000621,
        "PLN" to 4.306022,
        "EUR" to 1.0
    )

    private val random = Random()

    private fun noise(): Map<String, Double> {
        val res: MutableMap<String, Double> = hashMapOf()
        for (c in map) {
            res[c.key] = (random.nextDouble() * 0.01 + 1) * c.value
        }
        return res
    }



}
