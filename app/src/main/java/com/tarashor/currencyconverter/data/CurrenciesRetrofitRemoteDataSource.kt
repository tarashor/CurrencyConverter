package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrenciesRetrofitRemoteDataSource(private val service: APIService)
    : ICurrenciesDataSource {

    override fun getCurrencies(baseCurrency: String?): Flowable<CurrenciesDTO> {
        return service.getCurrencies(baseCurrency)
    }
}

