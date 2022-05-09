package com.liveearth.streetview.navigation.map.worldradio.locationTracking

import android.annotation.SuppressLint
import android.content.*
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityLocationTrackingMainBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.TrackingLocationItemAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.TrackingLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.*
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView.ACTION_PLAY
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView.ACTION_STOP
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView.MY_TIMER_BROADCAST
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import java.lang.reflect.Type

class LocationTrackingMainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding:ActivityLocationTrackingMainBinding

    private val TAG = "LocationTracking"
    private lateinit var preferences: SharedPreferences
    private var latitude: Double = 0.0
    private var longitude: Double = -0.0
    private lateinit var mapView: MapView
    private lateinit var mBuildingPlugin: BuildingPlugin
    lateinit var mapbox: MapboxMap
    private var myStyle: Style? = null
    private var locationMarker: Marker? = null
    var zoom: Int = 16
    var flagMap: Boolean = true
    private lateinit var myRepositoryStreetView: LocationRepositoryStreetView
    var mBottomSheetBehavior: BottomSheetBehavior<View>?=null
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass
    private lateinit var mAdapter : TrackingLocationItemAdapter
    var mTrackingList = ArrayList<TrackingLocationModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityLocationTrackingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = findViewById(R.id.mapViewTracking)
        mapView.onCreate(savedInstanceState)
        mPreferenceManagerClass = PreferenceManagerClass(this)

        mapViewResult()
        clickListenerTracking()


    }
    private fun mapViewResult() {
        mapView.getMapAsync(this)
    }
    @SuppressLint("MissingPermission")
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapbox = mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.setStyle(Style.SATELLITE) { style ->
            myStyle = style
            mBuildingPlugin = BuildingPlugin(mapView, mapboxMap, style)
            mBuildingPlugin.setMinZoomLevel(15f)

            val locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build())
            locationComponent.isLocationComponentEnabled = true
            locationComponent.renderMode = RenderMode.NORMAL
        }

        getYourCurrentLocation()

    }

    private fun getYourCurrentLocation() {
        val tempMapbox = mapbox
        try {
            myRepositoryStreetView = LocationRepositoryStreetView(this, object :
                MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        LocationHelperAssistant.setZoomMarker(location.latitude, location.longitude, tempMapbox, zoom)
                        myRepositoryStreetView.stopLocation()
                    } else {
                        myRepositoryStreetView.startLocation()
                    }
                }
            })
        } catch (e: Exception) {
            Log.d(TAG, "Exception$e")
        }

    }

    private fun clickListenerTracking() {

        val broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                val getSpeed  = intent!!.getFloatExtra("speed",0.0f)
                //binding.speedTx.text = getSpeed.toString()
                 Log.d(TAG,"=======activity==="+getSpeed)
            }
        }

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter(MY_TIMER_BROADCAST))

        binding.startTrackingLocation.setOnClickListener {
            val intentService = Intent(this,LocationTrackingService::class.java)
            intentService.action = ACTION_PLAY
            startService(intentService)
        }

        binding.stopBtn.setOnClickListener {
            val intentService = Intent(this, LocationTrackingService::class.java)
            intentService.action = ACTION_STOP
            startService(intentService)
            //showTRackingData()
        }
    }

    private fun showTRackingData() {
        try {
            preferences= getSharedPreferences(ConstantsStreetView.sharedPrefName, Context.MODE_PRIVATE)
            val gson = Gson()
            val jsonString = preferences.getString("mTrackingList",null)
            val type = object : TypeToken<ArrayList<TrackingLocationModel>>() {}.type
            mTrackingList = gson.fromJson(jsonString, type)

            mAdapter = TrackingLocationItemAdapter(mTrackingList,this)
/*
            binding.locationRecycler.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@LocationTrackingMainActivity)
                adapter = mAdapter
            }*/
        } catch (e: Exception) {
        }
    }


}