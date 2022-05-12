package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrackLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackingLocation(model: TrackLocationModel)

    @Update
    suspend fun updateTrackingLocation(model: TrackLocationModel)

    @Delete
    suspend fun deleteTrackingLocation(model: TrackLocationModel)

    @Query("DELETE FROM TrackingLocation_table WHERE id = :id")
    suspend fun deleteTrackingById(id:Int)

    @Query("SELECT * FROM TrackingLocation_table WHERE time = :time")
    suspend fun getDataByDate(time:String): TrackLocationModel?

    @Query("SELECT * FROM TrackingLocation_table WHERE id = :id")
    suspend fun getDataById(id:Int): TrackLocationModel?

    @Query("SELECT * FROM TrackingLocation_table")
    fun gatAllData():LiveData<List<TrackLocationModel>>

    @Query("DELETE FROM TrackingLocation_table")
    suspend fun clearNote()

}