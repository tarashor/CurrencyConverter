package com.tarashor.currencyconverter.core

import io.reactivex.Scheduler

interface ISchedulerProvider {
    val io : Scheduler
    val main : Scheduler
    val computation : Scheduler

}
