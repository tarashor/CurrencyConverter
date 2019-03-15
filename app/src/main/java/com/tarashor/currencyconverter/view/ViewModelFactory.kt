package com.tarashor.currencyconverter.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.tarashor.currencyconverter.data.CurrenciesRepository
import com.tarashor.currencyconverter.data.CurrenciesRetrofitRemoteDataSource
import com.tarashor.currencyconverter.data.ICurrenciesRepository
import com.tarashor.currencyconverter.viewmodel.CurrencyConverterViewModel
import java.lang.reflect.InvocationTargetException

class ViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (CurrencyConverterViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                return modelClass.getConstructor(ICurrenciesRepository::class.java).newInstance(createCurrencyRepository())
            } catch (e: IllegalAccessException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: InstantiationException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            } catch (e: InvocationTargetException) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        }

        throw RuntimeException("Cannot create an instance of $modelClass")
    }

    private fun createCurrencyRepository(): ICurrenciesRepository {
        return CurrenciesRepository(CurrenciesRetrofitRemoteDataSource())
    }

}
