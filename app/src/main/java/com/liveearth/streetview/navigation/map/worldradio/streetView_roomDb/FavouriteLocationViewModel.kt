package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liveearth.streetview.navigation.map.worldradio.roomdatabase.StreetViewDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteLocationViewModel(mContext: Context) : ViewModel() {

    private val repository = FavouriteLocationRepository(StreetViewDatabase(mContext))

    fun insertFavouriteLocation(model: FavouriteLocationModel) {
        viewModelScope.launch {
            Dispatchers.IO
            repository.insertFavouriteLocation(model)
        }
    }

    suspend fun getDataById(id: String):FavouriteLocationModel? = repository.getDataById(id)

    suspend fun updateFavouriteLocation(model: FavouriteLocationModel) = repository.updateFavouriteLocation(model)
    suspend fun deleteFavouriteLocation(model: FavouriteLocationModel) = repository.deleteFavouriteLocation(model)

    suspend fun clearSpeed() = repository.clearSpeed()

    fun getAllData() = repository.getAllData()

    fun stopObsover() {
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
    }

}