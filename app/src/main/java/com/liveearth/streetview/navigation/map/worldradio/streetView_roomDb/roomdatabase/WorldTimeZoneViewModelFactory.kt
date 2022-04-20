package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.roomdatabase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class WorldTimeZoneViewModelFactory constructor(private val repository: WorldTimeZoneRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WorldTimeZoneViewModel::class.java)) {
            WorldTimeZoneViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
