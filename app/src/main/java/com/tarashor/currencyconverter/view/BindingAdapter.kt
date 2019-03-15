package com.tarashor.currencyconverter.view

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.tarashor.currencyconverter.viewmodel.CurrenciesAdapterModel
import com.tarashor.currencyconverter.viewmodel.CurrencyViewModel

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("items")
    fun setSearchItems(recyclerView: RecyclerView, items: CurrenciesAdapterModel?) {
        if (recyclerView.adapter is CurrenciesAdapter) {
            (recyclerView.adapter as CurrenciesAdapter).submitList(items?.build())
        }
    }
}