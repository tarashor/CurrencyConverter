package com.tarashor.currencyconverter.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tarashor.currencyconverter.R
import com.tarashor.currencyconverter.ui.viewmodel.CurrencyViewModelItem

class CurrencyViewHolder(itemView: View,
                         val onCurrencySelected:(CurrencyViewModelItem)->Unit,
                         val onCurrencyChanged:(CurrencyViewModelItem)->Unit)
    : RecyclerView.ViewHolder(itemView) {

    protected val idTextView : TextView = itemView.findViewById(R.id.id_tv)
    protected val editText : EditText = itemView.findViewById(R.id.value_edt)

    var currencyViewModel: CurrencyViewModelItem? = null

    private var isTextSetsProgramatically = false

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!isTextSetsProgramatically) {
                currencyViewModel?.let{
                    it.amount = CurrenciesAdapter.parseAmount(s.toString())
                    onCurrencyChanged(it)
                }
            }
        }
    }

    fun setCurrencyOnly(currency: CurrencyViewModelItem) {
        idTextView.text = currency.currency
    }


    fun setAmountOnly(currency: CurrencyViewModelItem) {
        isTextSetsProgramatically = true
        editText.setTextKeepState(
            CurrenciesAdapter.formatAmount(
                currency.amount
            )
        )
        isTextSetsProgramatically = false
    }

    fun setIsSelectedOnly(selected: Boolean) {
        if (selected) {
            editText.addTextChangedListener(textWatcher)
            itemView.setOnClickListener(null)
            editText.setOnFocusChangeListener(null)
        } else {
            editText.removeTextChangedListener(textWatcher)
            itemView.setOnClickListener {
                editText.requestFocus()
                editText.setSelection(editText.text.length)
            }

            editText.setOnFocusChangeListener { v, hasFocus ->
                if (currencyViewModel != null) {
                    if (hasFocus) onCurrencySelected(currencyViewModel!!)
                }
            }
        }
    }

    fun willBeShown() {
        currencyViewModel?.let{
            if (it.isSelected)
                editText.requestFocus()
        }
    }

    fun willBeHidden() {
        editText.clearFocus()
    }



}