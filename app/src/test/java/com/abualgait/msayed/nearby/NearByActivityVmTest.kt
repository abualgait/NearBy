package com.abualgait.msayed.nearby

import com.abualgait.msayed.nearby.ui.nearbyplacesactivity.NearByActivityVm
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.koin.java.KoinJavaComponent

class NearByActivityVmTest : BaseUnitTest() {
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

}