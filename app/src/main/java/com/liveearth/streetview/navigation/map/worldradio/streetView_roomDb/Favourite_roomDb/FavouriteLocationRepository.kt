package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb

import androidx.lifecycle.LiveData
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.roomdatabase.StreetViewDatabase

class FavouriteLocationRepository(
    private val mDatabase: StreetViewDatabase
) {
    suspend fun insertFavouriteLocation(model: FavouriteLocationModel) =
        mDatabase.getFavouriteLocationDao().insertFavouriteLocation(model)

    suspend fun updateFavouriteLocation(model: FavouriteLocationModel) =
        mDatabase.getFavouriteLocationDao().updateFavouriteLocation(model)

    suspend fun getDataById(id: String): FavouriteLocationModel? =
        mDatabase.getFavouriteLocationDao().getDataById(id)

    suspend fun deleteFavouriteLocation(model: FavouriteLocationModel) =
        mDatabase.getFavouriteLocationDao().deleteFavouriteLocation(model)

    suspend fun deleteFavouriteById(locationId: String) =
        mDatabase.getFavouriteLocationDao().deleteFavouriteById(locationId)

    suspend fun clearSpeed() = mDatabase.getFavouriteLocationDao().clearNote()

    fun getAllData(): LiveData<List<FavouriteLocationModel>> = mDatabase.getFavouriteLocationDao().gatAllData()

}