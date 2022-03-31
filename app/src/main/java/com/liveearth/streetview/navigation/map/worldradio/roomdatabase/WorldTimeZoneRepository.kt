package com.liveearth.streetview.navigation.map.worldradio.roomdatabase

import androidx.lifecycle.LiveData

class WorldTimeZoneRepository(
    private val liveEarthDatabase: StreetViewDatabase
) {
    suspend fun insertTimeZone(modelTimeZone: WordTimeZoneModel) =
        liveEarthDatabase.getSpeedDao().insertTimeZone(modelTimeZone)

    suspend fun updateTimeZone(modelTimeZone: WordTimeZoneModel) =
        liveEarthDatabase.getSpeedDao().updateTimeZone(modelTimeZone)

    suspend fun deleteTimeZone(modelTimeZone: WordTimeZoneModel) =
        liveEarthDatabase.getSpeedDao().deleteTimeZone(modelTimeZone)

    suspend fun clearSpeed() = liveEarthDatabase.getSpeedDao().clearNote()

    fun getAllData(): LiveData<List<WordTimeZoneModel>> = liveEarthDatabase.getSpeedDao().gatAllData()


}