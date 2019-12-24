package com.abualgait.msayed.nearby.shared.interfaces

import android.location.Location
import com.google.android.gms.common.api.ResolvableApiException


interface ILocationCallbacks {


    fun getCurrentLocation(currentLocation: Location)

    fun checkLocationSettings(rae: ResolvableApiException)

}