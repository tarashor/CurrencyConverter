package com.tarashor.currencyconverter.ui.view

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tarashor.currencyconverter.databinding.CurrencyConverterFragmentBinding

import com.tarashor.currencyconverter.ui.adapter.CurrenciesAdapter

import com.tarashor.currencyconverter.ui.viewmodel.CurrencyConverterViewModel
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class CurrencyConverterFragment : DaggerFragment() {

    companion object {
        fun newInstance() = CurrencyConverterFragment()
    }

    @Inject
    lateinit var viewModelsFactory: ViewModelProvider.Factory

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
            { currency ->
                viewModel.updateSelectedCurrency(currency)
            },
            { currency ->
                viewModel.updateAmount(currency.amount)
            })

        binding.currenciesList.preserveFocusAfterLayout = true

        binding.currenciesList.layoutManager = LinearLayoutManager(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelsFactory).get(CurrencyConverterViewModel::class.java)

        binding.viewModel = viewModel
    }


}
