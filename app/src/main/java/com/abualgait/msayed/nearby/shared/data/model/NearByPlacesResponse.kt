package com.abualgait.msayed.nearby.shared.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.abualgait.msayed.nearby.shared.databases.VenueLocationConverter
import com.google.gson.annotations.SerializedName


data class NearByPlacesResponse(
    @SerializedName("meta")
    var meta: Meta,
    @SerializedName("response")
    var response: Response
)

data class Meta(
    @SerializedName("code")
    var code: Int,
    @SerializedName("requestId")
    var requestId: String
)

data class Response(
    @SerializedName("venues")
    var venues: List<Venue>,
    @SerializedName("requestId")
    var confident: Boolean
)

@Entity
data class Venue(
    @PrimaryKey
    @SerializedName("id")
    var id: String,

    @SerializedName("name")
    var name: String,

    @TypeConverters(VenueLocationConverter::class)
    @SerializedName("location")
    var location: VenueLocation,

    @SerializedName("referralId")
    var image: String

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Venue

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}


data class VenueLocation(
    //i used cc instead of address cause not all objects has key address
    @SerializedName("cc")
    var address: String
)
