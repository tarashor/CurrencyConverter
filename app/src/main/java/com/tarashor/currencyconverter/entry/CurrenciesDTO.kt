package com.tarashor.currencyconverter.entry

data class CurrenciesDTO(val base:String,
                         val rates: Map<String, Double>)