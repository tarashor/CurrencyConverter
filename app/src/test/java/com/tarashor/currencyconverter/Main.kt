package com.zenjob.android.browsr.utils

import kotlinx.coroutines.*

fun main(){
    listOf(
//        ::main1,
//        ::main2,
//        ::main3,
        ::main4,
    ).forEach { function ->
        Printer.reset()
        Printer.print("======== STARTED : ${function.name} ! ==========")
        function.invoke()
    }

}


private fun main1(){

    GlobalScope.launch {
        println("Scope launch: $this")
        println("Thread2 = " + Thread.currentThread().name)
        delay(1000)
        println("Thread3 = " + Thread.currentThread().name)
        Printer.print("World!")
    }


    println("Thread1 = " + Thread.currentThread().name)
    Printer.print("Hello, ")
    Thread.sleep(1500)
    Printer.print("War")
}

private fun main2(){

    GlobalScope.launch {
        println("Thread2 = " + Thread.currentThread().name)
        Thread.sleep(1000)
        println("Thread3 = " + Thread.currentThread().name)
        Printer.print("World!")
    }


    println("Thread1 = " + Thread.currentThread().name)
    Printer.print("Hello, ")
    Thread.sleep(1500)
    Printer.print("War")
}

private fun main3(){

    runBlocking {
        println("Thread2 = " + Thread.currentThread().name)
        delay(1000)
        println("Thread3 = " + Thread.currentThread().name)
        Printer.print("World!")
    }


    println("Thread1 = " + Thread.currentThread().name)
    Printer.print("Hello, ")
    Thread.sleep(1500)
    Printer.print("War")


}

private fun main4() = runBlocking {

    println("Scope main: $this")
    println("Context main: ${this.coroutineContext}")
    launch(Dispatchers.IO + CoroutineName("launch ")) {
        println("Scope launch: $this")
        println("Context launch: ${this.coroutineContext}")
        println("Thread2 = " + Thread.currentThread().name)
        delay(1000)
        println("Thread3 = " + Thread.currentThread().name)
        Printer.print("World!")
    }


    println("Thread1 = " + Thread.currentThread().name)
    Printer.print("Hello, ")
    delay(1500)
    Printer.print("War")
}




object Printer{
    private var start = System.currentTimeMillis()

    fun reset(){
        start = System.currentTimeMillis()
    }

    fun print(message: String){
        val current = System.currentTimeMillis()
        kotlin.io.print("[Time: ${current - start}]")

        println(message)
    }
}