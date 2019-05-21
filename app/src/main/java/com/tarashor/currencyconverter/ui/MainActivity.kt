package com.tarashor.currencyconverter.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tarashor.currencyconverter.R
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerAppCompatActivity_MembersInjector
import javax.inject.Inject

class MainActivity:AppCompatActivity(){ //: DaggerAppCompatActivity() {
    private val TAG = "MainActivity"

    @Inject
    lateinit var s:String

    @Inject
    lateinit var b:Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.v(TAG, s)
    }
}
