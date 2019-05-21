package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.entry.CurrenciesDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("latest")
    fun getCurrencies(@Query("base") baseCurrency: String?): Call<CurrenciesDTO>
}