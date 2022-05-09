package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class TrackLocationViewModelFactory constructor(private val mContext: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(TrackLocationViewModel::class.java)) {
            TrackLocationViewModel(mContext) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
