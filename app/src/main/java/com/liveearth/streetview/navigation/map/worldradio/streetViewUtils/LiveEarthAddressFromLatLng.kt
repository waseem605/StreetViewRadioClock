package com.example.centurionnavigation.callBack

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LiveEarthAddressFromLatLng : CoroutineScope {

    private val TAG = "CoroutineTask:"
    private var job: Job = Job()
    private var mContext: Context
    private var callBack: GeoTaskCallback
    private var latLng: LatLng

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }

    constructor(mContext: Context, latLng: LatLng, callBack:GeoTaskCallback) {
        this.mContext = mContext
        this.callBack = callBack
        this.latLng = latLng
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    fun execute() = launch { /*launch is having main thread scope*/
        Log.d(TAG, "execute: ${Thread.currentThread().name}")
        onPreExecute()
        val result = doInBackground() // runs in background thread without blocking the Main Thread
        onPostExecute(result)
    }

    /*it will done then next will call*/
    private suspend fun doInBackground(): String =
        withContext(Dispatchers.IO+ coroutineExceptionHandler) { // to run code in Background Thread
            Log.d(TAG, "doInBackground: ${Thread.currentThread().name}")
            // delay(1000)
            var location = "User Location"
            val geocoderHelper = Geocoder(mContext)
            val listAddressed: ArrayList<Address>
            if (Geocoder.isPresent() && isInternetAvailable()) {
                try {
                    listAddressed = geocoderHelper.getFromLocation(
                        latLng.latitude,
                        latLng.longitude,
                        1
                    ) as ArrayList<Address>
                    if (listAddressed.size > 0) {
                        ConstantsStreetView.currentCountryCode=listAddressed[0].countryCode
                        ConstantsStreetView.currentCountryName=listAddressed[0].countryName
                        location = listAddressed[0].getAddressLine(0)
                    }
                } catch (e: Exception) {
                }
            }
            return@withContext location
        }

    // Runs on the Main(UI) Thread
    private fun onPreExecute() {
        Log.d(TAG, "onPreExecute: ${Thread.currentThread().name}")
        // show progress
    }

    private suspend fun checkMethod() = withContext(Dispatchers.IO) {
        Log.d(TAG, "checkMethod: Entered")
        delay(5000)
        Log.d(TAG, "checkMethod: Exited")
    }

    // Runs on the Main(UI) Thread
    private fun onPostExecute(result: String) {

        Log.d(TAG, "onPostExecute: Result: $result ${Thread.currentThread().name}")
        Log.d(TAG, "onPostExecute: Job is C ${job.isCancelled}")
        Log.d(TAG, "onPostExecute: Job is A ${job.isActive}")

        if (result!=null){
            callBack.onSuccessLocationFetched(result)
        }else {
            Log.d(TAG, "onPostExecute: Result Failed")
            callBack.onFailedLocationFetched()
        }
        cancel()
        Log.d(TAG, "onPostExecute: Job is C ${job.isCancelled}")
        Log.d(TAG, "onPostExecute: Job is A ${job.isActive}")
    }


    // call this method to cancel a coroutine when you don't need it anymore,
    // e.g. when user closes the screen
    private fun cancel() {
        Log.d(TAG, "cancel ${Thread.currentThread().name}")
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
        fun onSuccessLocationFetched(fetchedAddress: String?)
        fun onFailedLocationFetched()
    }

}