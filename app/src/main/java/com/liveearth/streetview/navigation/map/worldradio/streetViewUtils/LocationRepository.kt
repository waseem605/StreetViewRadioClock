package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.example.centurionnavigation.dialogs.LocationEnableRequestDialogueBox
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener


class LocationRepository(context: Context, myListener: MyLocationListener) {
     var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var mContext:Context?=null
    private var myListener: MyLocationListener? =null

    val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 1000
        //numUpdates = 2
       /* numUpdates = 100
        setNumUpdates(100)*/
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }
    
    init {
        mContext=context
        this.myListener=myListener

        val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            startLocation()
        }else {
            val dialog = LocationEnableRequestDialogueBox(context)
            dialog.show()
        }
    }

    companion object {

//        fun toStopLocation(){
//
//        }
    }

    @SuppressLint("MissingPermission")
    fun startLocation(){
        Log.i("setLocationData","onActive : ")
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }
    
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private fun setLocationData(location: Location) {
        Log.i("setLocationData",":1122 "+location.longitude.toString())
        myListener!!.onLocationChanged(location)
        //to stop refreshing location
      //  stopLocation()

    }
    
    fun stopLocation(){
        Log.i("setLocationData","onInactive : ")
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

}