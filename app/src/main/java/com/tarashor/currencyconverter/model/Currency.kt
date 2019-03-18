package com.tarashor.currencyconverter.model

class Currency(val id: String)
    : Comparable<Currency> {

    override fun compareTo(other: Currency) =  id.compareTo(other.id)

    override fun equals(other: Any?): Boolean {
        return other is Currency && id.equals(other.id)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return id
    }

}