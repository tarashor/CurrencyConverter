package com.hellofresh.task2.di

import dagger.Component
import dagger.Subcomponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
private class A @Inject constructor()

@DependencyScope
private class B @Inject constructor(val a: A)

@Singleton
@Component
private interface MainComponent{
    fun subcomponent(): SubComponent
}

@DependencyScope
@Subcomponent
private interface SubComponent{
    fun b(): B
}

fun main() {
    val rootComponent = DaggerMainComponent.create()
    val subComponent = rootComponent.subcomponent()

    val d = subComponent.b()

    println("d.root = ${d.a}")
    println(d)
}
