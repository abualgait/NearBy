package com.abualgait.msayed.nearby


import android.content.Context
import com.abualgait.msayed.nearby.di.testApp
import com.abualgait.msayed.nearby.shared.databases.DBRepository
import com.abualgait.msayed.nearby.shared.koin.appModule
import com.abualgait.msayed.nearby.shared.util.SharedPref
import com.abualgait.msayed.nearby.ui.nearbyplacesactivity.nearbyActivityModule
import org.junit.Before
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.test.AutoCloseKoinTest
import org.koin.test.mock.declareMock
import org.mockito.Mock

import org.mockito.Mockito
import org.mockito.MockitoAnnotations

open class BaseUnitTest: AutoCloseKoinTest() {

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)
        startKoin {
            androidContext(Mockito.mock(Context::class.java))
            printLogger(Level.DEBUG)
            modules(testApp)

            declareMock<SharedPref> {

            }
            declareMock<DBRepository> {

            }
        }
    }
}