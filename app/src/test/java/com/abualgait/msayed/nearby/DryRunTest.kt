package com.abualgait.msayed.nearby

import com.abualgait.msayed.nearby.di.testApp
import org.junit.Test
import org.koin.core.context.startKoin

class DryRunTest : BaseUnitTest() {

    @Test
    fun testConfiguration() {
        startKoin { modules(testApp) }
    }

}