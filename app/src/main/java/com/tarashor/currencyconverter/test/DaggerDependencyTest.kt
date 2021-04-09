package com.hellofresh.task2.di

import dagger.Component
import javax.inject.Inject
import javax.inject.Scope
import javax.inject.Singleton

@Singleton
private class Root1 @Inject constructor()

private class Root2 @Inject constructor()

@DependencyScope
private class Dependent @Inject constructor(val root1: Root1, val root2: Root2)

@Singleton
@Component
private interface Root1Component{
    fun root():Root1
}

@Component
private interface Root2Component{
    fun root():Root2
}

@Component(dependencies = [Root1Component::class, Root2Component::class])
@DependencyScope
private interface DependentComponent{
    fun dependent(): Dependent
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class DependencyScope

fun main() {
    val root1Component = DaggerRoot1Component.create()
    val root2Component = DaggerRoot2Component.create()
    val dependentComponent = DaggerDependentComponent
            .builder()
            .root1Component(root1Component)
            .root2Component(root2Component)
            .build()

    val r1 = root1Component.root()
    val r2 = root2Component.root()
    val d = dependentComponent.dependent()

    println("root1 = $r1")
    println("d.root1 = ${d.root1}")
    println("root2 = $r2")
    println("d.root2 = ${d.root2}")
    println(d)
}