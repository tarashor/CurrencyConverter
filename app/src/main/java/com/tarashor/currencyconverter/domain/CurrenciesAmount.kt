package com.tarashor.currencyconverter.domain

data class CurrenciesAmount(val baseCurrency: String,
                            val amount: Double,
                            val amounts: Map<String, Double>)