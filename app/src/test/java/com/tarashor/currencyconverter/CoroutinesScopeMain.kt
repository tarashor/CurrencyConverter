package com.zenjob.android.browsr.utils

import kotlinx.coroutines.*

val scope = CoroutineScope(Dispatchers.Default)

fun main() = runBlocking {
    println("Scope main: $this")
    doWork2()
    println("main ended")
}

suspend fun doWork() {
    Printer.print("start")
    val scope = supervisorScope {
        println("Scope cor: $this")
        launch {
            println("Scope launch1: $this")
            delay(1000)
            Printer.print("part1")
        }

        launch {
            println("Scope launch2: $this")
            delay(2000)
            Printer.print("part2")
        }
    }
}


suspend fun doWork2() {
    Printer.print("start")


        scope.launch {
            println("Scope launch1: $this")
            delay(1000)
            Printer.print("part1")
        }

        scope.launch {
            println("Scope launch2: $this")
            delay(2000)
            Printer.print("part2")
        }

}



