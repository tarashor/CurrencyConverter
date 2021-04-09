package com.hellofresh.task2.di

import java.lang.reflect.Proxy
import java.util.*

fun main() {
    // Setup... this is where the magic happens...
    val service = Proxy.newProxyInstance(
            ApiInterface::class.java.classLoader, arrayOf<Class<*>>(ApiInterface::class.java)
    ) { proxy, method, args1 ->
        // we control what happens when the method is called (invoked), we get all the necessary information
        println("invoking method " + method.name + " with arguments " + Arrays.toString(args1))
        // this next part is just to show how you can manipulate return value
        if (args1.isNotEmpty() && args1[0] is String) {
            return@newProxyInstance (args1[0] as String).toUpperCase()
        }
        null
    } as ApiInterface

    // CLIENT CODE we call both methods in the ApiInterface
    println("returned: " + service.getSomeStringFromServer("Hello"))
    service.doSomeOtherThing(4.5)
}

internal interface ApiInterface {
    fun getSomeStringFromServer(parameter: String?): String
    fun doSomeOtherThing(otherParam: Double)
}