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
import com.tarashor.currencyconverter.viewmodel.CurrencyViewModelItem
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat

class CurrenciesAdapter (
    private val onCurrencySelected:(CurrencyViewModelItem)->Unit,
    private val onCurrencyChanged:(CurrencyViewModelItem)->Unit) :

    ListAdapter<CurrencyViewModelItem, CurrencyViewHolder>(object: DiffUtil.ItemCallback<CurrencyViewModelItem>(){
        override fun areItemsTheSame(p0: CurrencyViewModelItem, p1: CurrencyViewModelItem): Boolean {
            return p0.currency.compareTo(p1.currency) == 0
        }

        override fun areContentsTheSame(p0: CurrencyViewModelItem, p1: CurrencyViewModelItem): Boolean {
            return p0.currency.compareTo(p1.currency) == 0
                    && p0.amount == p1.amount
                    && p0.isSelected == p1.isSelected
        }

        override fun getChangePayload(oldItem: CurrencyViewModelItem, newItem: CurrencyViewModelItem): Any? {
            var payload = 0
            if (oldItem.currency.compareTo(newItem.currency) != 0) {
                payload = payload or CURRENCY_CHANGED
            }

            if (oldItem.amount != newItem.amount) {
                payload = payload or AMOUNT_CHANGED
            }

            if (oldItem.isSelected != newItem.isSelected) {
                payload = payload or SELECTION_CHANGED
            }
            return payload
        }

    }) {


    override fun onBindViewHolder(p0: CurrencyViewHolder, p1: Int) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return CurrencyViewHolder(itemView, onCurrencySelected, onCurrencyChanged)
    }

    override fun onBindViewHolder(viewHolder: CurrencyViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNullOrEmpty()) {
            viewHolder.setCurrencyOnly(getItem(position))
            viewHolder.setAmountOnly(getItem(position))
            viewHolder.setIsSelectedOnly(getItem(position).isSelected)
        } else {
            for (payload in payloads) {
                if (payload is Int) {
                    if (payload and CURRENCY_CHANGED == CURRENCY_CHANGED) {
                        viewHolder.setCurrencyOnly(getItem(position))
                    }
                    if (payload and AMOUNT_CHANGED == AMOUNT_CHANGED) {
                        viewHolder.setAmountOnly(getItem(position))
                    }

                    if (payload and SELECTION_CHANGED == SELECTION_CHANGED) {
                        viewHolder.setIsSelectedOnly(getItem(position).isSelected)
                    }
                }
            }
        }
        viewHolder.currencyViewModel = getItem(position)
    }

    override fun onViewAttachedToWindow(holder: CurrencyViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.willBeShown()
    }

    override fun onViewDetachedFromWindow(holder: CurrencyViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.willBeHidden()
    }

    companion object {
        val CURRENCY_CHANGED = 1 shl 0
        val AMOUNT_CHANGED = 1 shl 1
        val SELECTION_CHANGED = 1 shl 2

        private val df: DecimalFormat = DecimalFormat("#.##")

        fun formatAmount(amount: Double): String {
            df.roundingMode = RoundingMode.CEILING
            return df.format(amount)
        }

        fun parseAmount(str: String) = try {
            df.parse(str).toDouble()
        } catch (e: Exception) {
            0.0
        }
    }
}


class CurrencyViewHolder(itemView: View,
                         val onCurrencySelected:(CurrencyViewModelItem)->Unit,
                         val onCurrencyChanged:(CurrencyViewModelItem)->Unit)
    : RecyclerView.ViewHolder(itemView) {

    protected val idTextView : TextView = itemView.findViewById(R.id.id_tv)
    protected val editText : EditText = itemView.findViewById(R.id.value_edt)

    private var isTextSetsProgramatically = false;

    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (!isTextSetsProgramatically) {
                currencyViewModel?.amount = CurrenciesAdapter.parseAmount(s.toString())
                onCurrencyChanged(currencyViewModel!!)
            }
        }
    }

    fun setCurrencyOnly(currency: CurrencyViewModelItem) {
        idTextView.text = currency.currency.id
    }


    fun setAmountOnly(currency: CurrencyViewModelItem) {
        isTextSetsProgramatically = true
        editText.setTextKeepState(CurrenciesAdapter.formatAmount(currency.amount))
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
                if (currencyViewModel != null){
                    editText.requestFocus()
                    editText.setSelection(editText.text.length)
                }
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
            if (it.isSelected) editText.requestFocus()
        }
    }

    fun willBeHidden() {
        editText.clearFocus()
    }


    var currencyViewModel:CurrencyViewModelItem? = null
}

