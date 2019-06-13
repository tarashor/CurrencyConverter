package com.tarashor.currencyconverter.domain

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.tarashor.currencyconverter.core.ISchedulerProvider
import com.tarashor.currencyconverter.data.CurrenciesRepository
import com.tarashor.currencyconverter.data.ICurrenciesDataSource
import com.tarashor.currencyconverter.data.ICurrenciesRepository
import com.tarashor.currencyconverter.entry.CurrenciesDTO
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.TimeUnit
import org.mockito.Mockito.`when`
import org.mockito.Mockito.notNull

class CurrenciesInteractorTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val response = CurrenciesDTO("EUR", mapOf(
        "UAH" to 29.423548
    ))

    val testScheduler = TestScheduler()

    @Mock
    lateinit var currenciesRepository: ICurrenciesRepository

    @Mock
    lateinit var schedulerProvider: ISchedulerProvider

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.doReturn(Flowable.just(response)).`when`(currenciesRepository).getCurrencies(ArgumentMatchers.notNull())
        Mockito.doReturn(testScheduler).`when`(schedulerProvider).computation

    }

    @Test
    fun loadCurrencies() {
        val interactor = CurrenciesInteractor(currenciesRepository, schedulerProvider)

        val baseCurrency = Observable.just("EUR")
        val amountBaseCurrency = Observable.just(200.0)


        val testObserver = interactor.loadCurrencies(baseCurrency, amountBaseCurrency)
            .test()

        testObserver.assertNotTerminated()
            .assertNoErrors()

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)

        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValueCount(2)
        testScheduler.advanceTimeBy(1L, TimeUnit.SECONDS)
        testObserver.assertValueCount(3)

        testObserver.dispose()

    }

    @Test
    fun testInterval(){
        val numbers = Observable.just(1)
        val ticks = Observable.interval(0, 10, TimeUnit.SECONDS, testScheduler)

        val testObserver = Observable.combineLatest(
            numbers,
            ticks,
            BiFunction<Int, Long, Pair<Int, Long>> { n, t -> Pair(n, t) })
            .doOnNext { System.out.println(it) }
            .test()

        testObserver.assertNotTerminated()
            .assertNoErrors()

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)

        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS)
        testObserver.assertValueCount(1)

        testScheduler.advanceTimeBy(10, TimeUnit.SECONDS)
        testObserver.assertValueCount(2)

        testObserver.dispose()
    }

    @Test
    fun testInterval2(){
        val startTime = System.currentTimeMillis()
        val observable1 = Observable.interval(0,5, TimeUnit.SECONDS)
        val observable2 = Observable.interval(0,10, TimeUnit.SECONDS)

        Observable.combineLatest(observable1, observable2,
            BiFunction<Long, Long, String> { observable1Times, observable2Times ->
                "Refreshed Observable1: $observable1Times refreshed Observable2: $observable2Times"
            })
            .subscribe {
                println("time = ${System.currentTimeMillis() - startTime}")
                println(it)
            }

        Thread.sleep(1000 * 20)
    }
}