package com.abualgait.msayed.nearby.shared.databases


class DBRepository(private val db: AppDatabase) {

    fun getVenueDao(): VenueItemDao? {
        return db.venueItemDao()
    }



}
