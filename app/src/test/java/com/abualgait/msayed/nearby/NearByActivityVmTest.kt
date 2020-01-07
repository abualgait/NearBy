package com.abualgait.msayed.nearby

import androidx.lifecycle.Observer
import com.abualgait.msayed.nearby.shared.network.ApiRepository
import com.abualgait.msayed.nearby.ui.nearbyplacesactivity.NearByActivityVm
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.koin.java.KoinJavaComponent
import org.koin.test.inject
import org.mockito.Mock
import org.mockito.Mockito

class NearByActivityVmTest : BaseUnitTest() {
    val repository by inject<ApiRepository>()
    private val postViewModel: NearByActivityVm by inject()
    private val latlng = "30.05611,31.23944"
    @Mock
    lateinit var listObserver: Observer<NearByActivityVm.ResultUIModel>

    @Test
    fun `check if latlng is not empty`() {
        assert(
            KoinJavaComponent.get(NearByActivityVm::class.java)
                .isValidLatLng(
                    KoinJavaComponent.get(NearByActivityVm::class.java).pref.latitude + "," + KoinJavaComponent.get(
                        NearByActivityVm::class.java
                    ).pref.longitude
                )
        )
    }

    @Test
    fun `check if latlng point is not origin point`() {
        assertEquals(
            KoinJavaComponent.get(NearByActivityVm::class.java)
                .isValidPoint("0.0"), false
        )
    }


    @Test
    fun testGetPosts() {
        repository.getNearByPlaces(latlng).blockingFirst()

        postViewModel.payload.observeForever(listObserver)
        postViewModel.getNearByPlaces(latlng)
        Mockito.verify(listObserver).onChanged(NearByActivityVm.ResultUIModel(isLoading = true))

        val value = postViewModel.payload.value ?: error("No value for view myModel")
        Mockito.verify(listObserver).onChanged(NearByActivityVm.ResultUIModel(list = value.list))

    }

    @Test
    fun testGetPostsFaild() {
        postViewModel.payload.observeForever(listObserver)
        postViewModel.getNearByPlaces(latlng)

        Mockito.verify(listObserver).onChanged(NearByActivityVm.ResultUIModel(isLoading = true))


        val error = IllegalStateException("Got an error !")
        Mockito.`when`(repository.getNearByPlaces(latlng)).thenReturn(Observable.error(error))

        Mockito.verify(listObserver).onChanged(NearByActivityVm.ResultUIModel(error = error))


    }


}