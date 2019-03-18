package com.tarashor.currencyconverter.data

import com.tarashor.currencyconverter.URL_BASE
import com.tarashor.currencyconverter.model.Currency
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.http.Query

class CurrenciesRetrofitRemoteDataSource : ICurrenciesDataSource{
    override fun getCurrencies(baseCurrency: Currency?, callback: (CurrenciesDAO?) -> Unit) {
        service.getCurrencies(baseCurrency?.id)
            .enqueue(object : Callback<CurrenciesDAO> {
                override fun onResponse(call: Call<CurrenciesDAO>, response: Response<CurrenciesDAO>) {
                    callback(response.body())
                }

                override fun onFailure(call: Call<CurrenciesDAO>, t: Throwable) {
                    callback(null)
                }
            })
    }

    private val service: APIRequest

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create<APIRequest>(APIRequest::class.java)
    }

}

interface APIRequest {
    @GET("latest")
    fun getCurrencies(@Query("base") baseCurrency: String?): Call<CurrenciesDAO>
}
