package com.tarashor.currencyconverter.model

class Currency(val id: String, val rateToBase: Double, val isBase: Boolean = false)
    : Comparable<Currency> {

    override fun compareTo(other: Currency): Int
            =   if (isBase && !other.isBase) 1
                else if (!isBase && other.isBase) -1
                else id.compareTo(other.id)

    override fun equals(other: Any?): Boolean {
        return other is Currency && id.equals(other.id)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return id
    }


    fun convertAmountToOtherCurrency(
        amount: Double,
        currency: Currency? = null
    ) : Double
            = amount / rateToBase * (currency?.rateToBase ?: 1.0)


}