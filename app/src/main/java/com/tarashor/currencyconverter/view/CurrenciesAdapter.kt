package com.tarashor.currencyconverter.view

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.tarashor.currencyconverter.R
import com.tarashor.currencyconverter.model.toDecimalString
import com.tarashor.currencyconverter.viewmodel.CurrencyViewModel
import java.lang.Exception

class CurrenciesAdapter (val onCurrencySelected:(CurrencyViewModel)->Unit,
                         val onCurrencyChnaged:(CurrencyViewModel)->Unit) :
    ListAdapter<CurrencyViewModel, CurrencyViewHolder>(object: DiffUtil.ItemCallback<CurrencyViewModel>(){
        override fun areItemsTheSame(p0: CurrencyViewModel, p1: CurrencyViewModel): Boolean {
            return p0.currency.compareTo(p1.currency) == 0 && p0.isSelected == p1.isSelected
        }

        override fun areContentsTheSame(p0: CurrencyViewModel, p1: CurrencyViewModel): Boolean {
            if (!p0.isSelected && !p1.isSelected)
                return  p0.currency.compareTo(p1.currency) == 0
                    && p0.amount == p1.amount
            else return p0.isSelected && p1.isSelected && p0.currency.compareTo(p1.currency) == 0
        }

        override fun getChangePayload(oldItem: CurrencyViewModel, newItem: CurrencyViewModel): Any? {
            var payload = 0
            if (oldItem.currency.compareTo(newItem.currency) != 0) {
                payload = payload or CURRENCY_CHANGED
            }

            if (oldItem.amount != newItem.amount) {
                payload = payload or AMOUNT_CHANGED
            }
            return payload
        }

    }) {

    val EDITABLE = 1
    val NORMAL = 0

    override fun onBindViewHolder(p0: CurrencyViewHolder, p1: Int) {
    }

    override fun getItemViewType(position: Int): Int =
        if (getItem(position).isSelected)
            EDITABLE
        else
            NORMAL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return when(viewType){
            EDITABLE -> EditableCurrencyViewHolder(itemView, onCurrencyChnaged)
            else -> NormalCurrencyViewHolder(itemView, onCurrencySelected)
        }
    }


    override fun onBindViewHolder(viewHolder: CurrencyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNullOrEmpty()) {
            viewHolder.setCurrencyOnly(getItem(position))
            viewHolder.setAmountOnly(getItem(position))
        } else {
            for (payload in payloads){
                if (payload is Int){
                    if (payload and CURRENCY_CHANGED == CURRENCY_CHANGED){
                        viewHolder.setCurrencyOnly(getItem(position))
                    }
                    if (payload and AMOUNT_CHANGED == AMOUNT_CHANGED){
                        viewHolder.setAmountOnly(getItem(position))
                    }
                }
            }
        }
        viewHolder.currencyViewModel = getItem(position)
    }

    companion object {
        val CURRENCY_CHANGED = 1 shl 0;
        val AMOUNT_CHANGED = 1 shl 1;
    }

}

class NormalCurrencyViewHolder(itemView: View,
                         val onCurrencySelected:(CurrencyViewModel)->Unit)
    : CurrencyViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            if (currencyViewModel != null) onCurrencySelected(currencyViewModel!!)
        }

        editText.setOnFocusChangeListener { v, hasFocus ->
            if (currencyViewModel != null && hasFocus)
                onCurrencySelected(currencyViewModel!!)
            v.clearFocus()
        }

    }
}

sealed class CurrencyViewHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    protected val idTextView : TextView = itemView.findViewById(R.id.id_tv)
    protected val editText : EditText = itemView.findViewById(R.id.value_edt)

    fun setCurrencyOnly(currency: CurrencyViewModel) {
        idTextView.text = currency.currency.id
    }

    open fun setAmountOnly(currency: CurrencyViewModel) {
        editText.setText(currency.amount.toDecimalString())
    }


    var currencyViewModel:CurrencyViewModel? = null


}

class EditableCurrencyViewHolder(itemView: View,
                         val onCurrencyChanged:(CurrencyViewModel)->Unit)
    : CurrencyViewHolder(itemView) {

    val textWatcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) { }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            try {
                val newAmount = java.lang.Double.parseDouble(s.toString())
                currencyViewModel?.amount = newAmount
                onCurrencyChanged(currencyViewModel!!)
            } catch (e: Exception) {
            }
        }
    }

    override fun setAmountOnly(currency: CurrencyViewModel) {
        editText.removeTextChangedListener(textWatcher)
        super.setAmountOnly(currency)
        editText.addTextChangedListener(textWatcher)
    }

}
