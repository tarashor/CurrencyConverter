package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrenciesRetrofitRemoteDataSource(private val service: APIService)
    : ICurrenciesDataSource {

    override fun getCurrencies(baseCurrency: String?, callback: (CurrenciesDTO?) -> Unit) {
        service.getCurrencies(baseCurrency)
            .enqueue(object : Callback<CurrenciesDTO> {
                override fun onResponse(call: Call<CurrenciesDTO>, response: Response<CurrenciesDTO>) {
                    callback(response.body())
                }

                override fun onFailure(call: Call<CurrenciesDTO>, t: Throwable) {
                    callback(null)
                }
            })
    }

}

