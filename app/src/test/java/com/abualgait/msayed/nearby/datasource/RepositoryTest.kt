package com.abualgait.msayed.nearby.datasource

import com.abualgait.msayed.nearby.shared.network.ApiRepository
import com.abualgait.msayed.nearby.di.testApp

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

class RepositoryTest : AutoCloseKoinTest() {

    val repository by inject<ApiRepository>()
    private val latlng = "30.05611,31.23944"
    @Before
    fun before() {
        startKoin { modules(testApp) }
    }


    @Test
    fun testCachedSearch() {

        val places1 = repository.getNearByPlaces(latlng).blockingFirst()
        val places2 = repository.getNearByPlaces(latlng).blockingFirst()
        assertEquals(places1, places2)
    }

    @Test
    fun testGetPostsSuccess() {

        repository.getNearByPlaces(latlng).test()
        val test = repository.getNearByPlaces(latlng).test()
        test.awaitTerminalEvent()
        test.assertComplete()
    }

    @Test
    fun testGetPostsFailed() {

        val test = repository.getNearByPlaces(latlng).test()
        test.awaitTerminalEvent()
        test.assertValue { response -> response.response.venues.isEmpty() }
    }
}