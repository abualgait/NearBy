package com.abualgait.msayed.nearby.shared.network


import com.abualgait.msayed.nearby.BuildConfig
import com.abualgait.msayed.nearby.shared.data.model.NearByPlacesResponse
import com.abualgait.msayed.nearby.shared.data.model.PhotosResponse
import com.abualgait.msayed.nearby.shared.util.configs.ConstValue
import io.reactivex.Observable


class ApiRepository(private val api: ApiInterface) {

    fun getNearByPlaces(latLng: String):
            Observable<NearByPlacesResponse> {
        return api.getPlaces(
            latLng,
            ConstValue.RADIUS,
            BuildConfig.CLIENTID,
            BuildConfig.CLIENT_SECRET,
            ConstValue.VERSION
        )
    }

    fun getPhotos(placeId: String):
            Observable<PhotosResponse> {
        return api.getPhotos(
            placeId,
            BuildConfig.CLIENTID,
            BuildConfig.CLIENT_SECRET,
            ConstValue.VERSION
        )
    }
}
