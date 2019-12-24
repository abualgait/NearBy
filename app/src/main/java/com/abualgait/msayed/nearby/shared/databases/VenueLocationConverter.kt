package com.abualgait.msayed.nearby.shared.databases

import androidx.room.TypeConverter
import com.abualgait.msayed.nearby.shared.data.model.VenueLocation

class VenueLocationConverter {

    @TypeConverter
    fun toVenueLocation(address: String?): VenueLocation? {
        return if (address == null) null else VenueLocation(address)
    }

    @TypeConverter
    fun toString(date: VenueLocation?): String? {
        return (date?.address)
    }
}