package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CurrentLatLngCoroutine(
    private var mContext: Context,
    private var latLngAddress: String,
    private var callBack: CurrentLatLngCallback
) : CoroutineScope {
    private var job: Job = Job()


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun execute() = launch {
        val result = doInBackground()
        onPostExecute(result)
    }

    private suspend fun doInBackground(): LatLng? =
        withContext(Dispatchers.IO) {
            var error="UnKnow"
            val geocoderHelper = Geocoder(mContext)
            val listAddressed: ArrayList<Address?>
            var location: Address? = null
            var latLng: LatLng? = null
            if (Geocoder.isPresent()) {
                try {
                    listAddressed = geocoderHelper.getFromLocationName(
                        latLngAddress,
                        5
                    ) as java.util.ArrayList<Address?>
                    if (listAddressed.size > 0) {
                        location = listAddressed[0]
                        latLng = LatLng(
                            location!!.latitude,
                            location.longitude
                        )
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    error=ex.message.toString()
                }
            }
            return@withContext latLng
        }


    private fun onPostExecute(result: LatLng?) {
        callBack.onSuccessLatLng(result)
        cancel()
    }


    private fun cancel() {
        job.cancel()
    }
}