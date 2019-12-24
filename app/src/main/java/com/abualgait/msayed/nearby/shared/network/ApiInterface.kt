package com.abualgait.msayed.nearby.shared.network


import com.abualgait.msayed.nearby.shared.data.model.NearByPlacesResponse
import com.abualgait.msayed.nearby.shared.data.model.PhotosResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiInterface {


    @GET("search")
    fun getPlaces(
        @Query("ll") latlng: String,
        @Query("radius") radius: String,
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("v") v: String
    ): Observable<NearByPlacesResponse>


    @GET("{id}/photos")
    fun getPhotos(
        @Path("id") id: String,
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("v") v: String

    ): Observable<PhotosResponse>


}