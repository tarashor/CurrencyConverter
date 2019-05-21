package com.tarashor.currencyconverter.ui.view

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import com.tarashor.currencyconverter.ui.adapter.CurrenciesAdapter
import com.tarashor.currencyconverter.ui.viewmodel.CurrencyViewModelItem

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("items")
    fun setSearchItems(recyclerView: RecyclerView, items: List<CurrencyViewModelItem>?) {
        if (recyclerView.adapter is CurrenciesAdapter) {
            (recyclerView.adapter as CurrenciesAdapter).submitList(items)
        }
    }
}