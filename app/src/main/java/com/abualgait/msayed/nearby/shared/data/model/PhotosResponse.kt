package com.abualgait.msayed.nearby.shared.data.model

import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("meta")
    var meta: Meta,
    @SerializedName("response")
    var response: PhotoRes


)

data class PhotoRes(
    @SerializedName("photos")
    var photos: PhotoPOJO

)

data class PhotoPOJO(
    @SerializedName("items")
    var items: List<ItemPOJO>

)


data class ItemPOJO(
    @SerializedName("prefix")
    var prefix: String,
    @SerializedName("suffix")
    var suffix: String,
    @SerializedName("width")
    var width: String,
    @SerializedName("height")
    var height: String
)