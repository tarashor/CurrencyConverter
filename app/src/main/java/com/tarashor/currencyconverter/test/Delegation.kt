package com.hellofresh.task2.di

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Delegation {
    val prop: Int by Helper()
    var prop2: Int by Helper2()
    val prop3: Int by lazyOf(4)
}

class Helper2 {
    operator fun setValue(thisRef: Delegation, property: KProperty<*>, value: Int) {
        TODO("Not yet implemented")
    }

    operator fun getValue(thisRef: Delegation, property: KProperty<*>): Int {
        TODO("Not yet implemented")
    }

}

class Helper : ReadOnlyProperty<Delegation, Int> {
    override fun getValue(thisRef: Delegation, property: KProperty<*>): Int {
        return 0
    }

}
