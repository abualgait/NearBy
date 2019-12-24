package com.abualgait.msayed.nearby.shared.databases

import androidx.room.*
import com.abualgait.msayed.nearby.shared.data.model.Venue

@Dao
interface VenueItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Venue)

    @Update
    fun updateItem(item: Venue)

    @Delete
    fun deleteItem(item: Venue)

    @Query("SELECT * FROM Venue WHERE id == :id")
    fun getItemById(id: Int): List<Venue>

    @Query("SELECT * FROM Venue")
    fun getItems(): List<Venue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertALLItems(items: MutableList<Venue>): List<Long>

    @Query("DELETE FROM Venue")
    fun deleteArticles()

}