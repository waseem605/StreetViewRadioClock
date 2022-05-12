package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.roomdatabase.StreetViewDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackLocationViewModel(mContext: Context) : ViewModel() {

    private val repository = TrackLocationRepository(StreetViewDatabase(mContext))

    fun insertTrackingLocation(model: TrackLocationModel) {
        viewModelScope.launch {
            Dispatchers.IO
            repository.insertTrackingLocation(model)
        }
    }

    suspend fun getDataByDate(dateString: String): TrackLocationModel? = repository.getDataByDate(dateString)
    suspend fun getDataById(id: Int): TrackLocationModel = repository.getDataById(id)!!

    fun updateTrackingLocation(model: TrackLocationModel){
        viewModelScope.launch {
            Dispatchers.IO
            repository.updateTrackingLocation(model)
        }
    }
    suspend fun deleteTrackingLocation(model: TrackLocationModel) = repository.deleteTrackingLocation(model)

    suspend fun deleteTrackingById(id: Int) = repository.deleteTrackingById(id)

    suspend fun clearSpeed() = repository.clearSpeed()

    fun getAllData() = repository.getAllData()

    fun stopObsover() {
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
    }

}