package com.tarashor.currencyconverter.core

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers

object SchedulerProvider : ISchedulerProvider {
    override val io: Scheduler
        get() = Schedulers.io()
    override val main: Scheduler
        get() = AndroidSchedulers.mainThread()
    override val computation: Scheduler
        get() = Schedulers.computation()
}