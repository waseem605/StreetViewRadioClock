package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb

import androidx.lifecycle.LiveData
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.roomdatabase.StreetViewDatabase

class TrackLocationRepository(
    private val mDatabase: StreetViewDatabase
) {
    suspend fun insertTrackingLocation(model: TrackLocationModel) =
        mDatabase.getTrackingDao().insertTrackingLocation(model)

    suspend fun updateTrackingLocation(model: TrackLocationModel) =
        mDatabase.getTrackingDao().updateTrackingLocation(model)

    suspend fun getDataById(id: Int): TrackLocationModel? =
        mDatabase.getTrackingDao().getDataById(id)

    suspend fun deleteTrackingLocation(model: TrackLocationModel) =
        mDatabase.getTrackingDao().deleteTrackingLocation(model)

    suspend fun deleteTrackingById(id: Int) =
        mDatabase.getTrackingDao().deleteTrackingById(id)

    suspend fun clearSpeed() = mDatabase.getTrackingDao().clearNote()

    fun getAllData(): LiveData<List<TrackLocationModel>> = mDatabase.getTrackingDao().gatAllData()

}