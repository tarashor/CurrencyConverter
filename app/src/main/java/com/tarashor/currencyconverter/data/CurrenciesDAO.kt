package com.tarashor.currencyconverter.data

data class CurrenciesDAO(val base:String,
                         val rates: Map<String, Double>)