package com.abualgait.msayed.nearby.shared.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.abualgait.msayed.nearby.shared.data.model.Venue

@Database(entities = [Venue::class], version = 2)
@TypeConverters(VenueLocationConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun venueItemDao(): VenueItemDao


}