package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.roomdatabase

import androidx.lifecycle.LiveData

class WorldTimeZoneRepository(
    private val mDatabase: StreetViewDatabase
) {
    suspend fun insertTimeZone(modelTimeZone: WordTimeZoneModel) =
        mDatabase.getTimeZoneDao().insertTimeZone(modelTimeZone)

    suspend fun updateTimeZone(modelTimeZone: WordTimeZoneModel) =
        mDatabase.getTimeZoneDao().updateTimeZone(modelTimeZone)

    suspend fun getDataByTimeZone(timeZone: String): WordTimeZoneModel? =
        mDatabase.getTimeZoneDao().getDataByTimeZone(timeZone)

    suspend fun deleteTimeZone(modelTimeZone: WordTimeZoneModel) =
        mDatabase.getTimeZoneDao().deleteTimeZone(modelTimeZone)

    suspend fun clearSpeed() = mDatabase.getTimeZoneDao().clearNote()

    fun getAllData(): LiveData<List<WordTimeZoneModel>> = mDatabase.getTimeZoneDao().gatAllData()


}