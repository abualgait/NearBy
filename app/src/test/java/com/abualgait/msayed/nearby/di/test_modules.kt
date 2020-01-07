package com.abualgait.msayed.nearby.di


import com.abualgait.msayed.nearby.shared.koin.appModule
import com.abualgait.msayed.nearby.ui.nearbyplacesactivity.nearbyActivityModule
import com.abualgait.msayed.nearby.util.TestSchedulerProvider
import org.koin.dsl.module


val testRxModule = module { single { TestSchedulerProvider() } }

val testApp = appModule + nearbyActivityModule  + testRxModule