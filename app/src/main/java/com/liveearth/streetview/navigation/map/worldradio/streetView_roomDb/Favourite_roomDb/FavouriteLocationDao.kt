package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavouriteLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteLocation(model: FavouriteLocationModel)

    @Update
    suspend fun updateFavouriteLocation(model: FavouriteLocationModel)

    @Delete
    suspend fun deleteFavouriteLocation(model: FavouriteLocationModel)

    @Query("SELECT * FROM FavouriteLocation_table WHERE locationId = :locationId")
    suspend fun getDataById(locationId:String): FavouriteLocationModel?

    @Query("SELECT * FROM FavouriteLocation_table")
    fun gatAllData():LiveData<List<FavouriteLocationModel>>

    @Query("DELETE FROM FavouriteLocation_table")
    suspend fun clearNote()

    @Query("DELETE FROM FavouriteLocation_table WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

}