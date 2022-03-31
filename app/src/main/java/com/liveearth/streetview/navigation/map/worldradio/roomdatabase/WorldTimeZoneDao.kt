package com.liveearth.streetview.navigation.map.worldradio.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel

@Dao
interface WorldTimeZoneDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeZone(modelTimeZone: WordTimeZoneModel)

    @Update
    suspend fun updateTimeZone(modelTimeZone: WordTimeZoneModel)

    @Delete
    suspend fun deleteTimeZone(modelTimeZone: WordTimeZoneModel)

    @Query("SELECT * FROM timeZoneWorld_table")
    fun gatAllData():LiveData<List<WordTimeZoneModel>>

    @Query("DELETE FROM timeZoneWorld_table")
    suspend fun clearNote()

    @Query("DELETE FROM timeZoneWorld_table WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

}