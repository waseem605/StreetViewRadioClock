package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.roomdatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorldTimeZoneViewModel(private val repository: WorldTimeZoneRepository) : ViewModel() {
    fun insertTimeZone(timeZoneModel: WordTimeZoneModel) {
        viewModelScope.launch {
            Dispatchers.IO
            repository.insertTimeZone(timeZoneModel)
        }
    }

    suspend fun getDataByTimeZone(timeZone: String):WordTimeZoneModel? = repository.getDataByTimeZone(timeZone)

    suspend fun updateTimeZone(timeZoneModel: WordTimeZoneModel) = repository.updateTimeZone(timeZoneModel)
    suspend fun deleteTimeZone(timeZoneModel: WordTimeZoneModel) = repository.deleteTimeZone(timeZoneModel)

    suspend fun clearSpeed() = repository.clearSpeed()

    fun getAllData() = repository.getAllData()

    fun stopObsover() {
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
    }

}