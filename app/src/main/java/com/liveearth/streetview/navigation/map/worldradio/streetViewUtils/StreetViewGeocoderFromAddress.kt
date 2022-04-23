package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class StreetViewGeocoderFromAddress : CoroutineScope {

    private val TAG = "RCoroutineTask:"
    private var job: Job = Job()
    private var mContext: Context
    private var callBack: GeoTaskCallback
    private var latLngAddress: String

    constructor(mContext: Context, latLngAddress: String, callBack: GeoTaskCallback) {
        this.mContext = mContext
        this.callBack = callBack
        this.latLngAddress = latLngAddress
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun execute() = launch { /*launch is having main thread scope*/
        Log.d(TAG, "execute:")
        onPreExecute()
        val result = doInBackground() // runs in background thread without blocking the Main Thread
        onPostExecute(result)
    }

    /*it will done then next will call*/
    private suspend fun doInBackground(): LatLng? =
        withContext(Dispatchers.IO) { // to run code in Background Thread
            Log.d(TAG, "doInBackground: ")
            val geocoderHelper = Geocoder(mContext)
            val listAddressed: ArrayList<Address?>
            var location: Address? = null
            var latLng: LatLng? = null
            if (Geocoder.isPresent() && isInternetAvailable()) {
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
                }
            }
            /*else{
                return@withContext null
            }*/
            return@withContext latLng
        }

    // Runs on the Main(UI) Thread
    private fun onPreExecute() {
        Log.d(TAG, "onPreExecute: ")
        // show progress
    }


    // Runs on the Main(UI) Thread
    private fun onPostExecute(result: LatLng?) {
        if (result != null) {
            Log.d(TAG, "onPostExecute: Result Success: $result")
            callBack.onSuccessLocationFetched(result)
        } else {
            Log.d(TAG, "onPostExecute: Result Failed")
            callBack.onFailedLocationFetched()
        }
        cancel()
    }

    // call this method to cancel a coroutine when you don't need it anymore,
    // e.g. when user closes the screen
    private fun cancel() {
        Log.d(TAG, "cancel")
        job.cancel()
    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        try {
            activeNetworkInfo = connectivityManager.activeNetworkInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    interface GeoTaskCallback {
        fun onSuccessLocationFetched(fetchedLatLngs: LatLng?)
        fun onFailedLocationFetched()
    }

}