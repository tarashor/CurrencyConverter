package com.tarashor.currencyconverter.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tarashor.currencyconverter.databinding.CurrencyConverterFragmentBinding

import com.tarashor.currencyconverter.viewmodel.CurrencyConverterViewModel

class CurrencyConverterFragment : Fragment() {

    companion object {
        fun newInstance() = CurrencyConverterFragment()
    }

    private lateinit var viewModel: CurrencyConverterViewModel
    private lateinit var binding: CurrencyConverterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CurrencyConverterFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.currenciesList.adapter = CurrenciesAdapter(
            {currency ->
                viewModel.updateSelectedCurrency(currency)
            },
            {currency -> viewModel.updateAmount(currency.amount)})

        binding.currenciesList.preserveFocusAfterLayout = true

        binding.currenciesList.layoutManager = LinearLayoutManager(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(CurrencyConverterViewModel::class.java)
        binding.viewModel = viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.startPollingCurrencyRates()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopPollingCurrencyRates()
    }

}
