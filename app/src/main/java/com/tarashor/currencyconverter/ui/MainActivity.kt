package com.tarashor.currencyconverter.ui

import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.tarashor.currencyconverter.R
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LocalBroadcastManager.getInstance(this)
    }
}
