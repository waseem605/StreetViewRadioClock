package com.liveearth.streetview.navigation.map.worldradio.locationTracking

import android.annotation.SuppressLint
import android.content.*
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelperAssistant.Companion.getCurrentDateTime
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationViewModelFactory
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import java.util.*
import kotlin.collections.ArrayList
import android.content.Intent

import android.content.IntentFilter
import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.LoadAdsStreetViewClock
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class LocationTrackingMainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityLocationTrackingMainBinding

    private val TAG = "LocationTracking"
    private lateinit var preferences: SharedPreferences
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private var mBackEndLatitude: Double = 0.0
    private var mBackEndLongitude: Double = 0.0
    private var mSpeedBackend: Float = 0F
    private var locationMarker: Marker? = null
    private lateinit var mapView: MapView
    private lateinit var mBuildingPlugin: BuildingPlugin
    lateinit var mapbox: MapboxMap
    private var myStyle: Style? = null
    var zoom: Int = 16
    private lateinit var myRepositoryStreetView: LocationRepositoryStreetView
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass
    private var mTrackingList = ArrayList<TrackingLocationModel>()
    private lateinit var mTrackLocationViewModel: TrackLocationViewModel

    private var mStart = true
    private var mStartTime = ""
    private var isFirstLocation = true

    private lateinit var mStartLocation: Location
    private lateinit var mEndLocation: Location
    private var mDistance: Double = 0.0
    private var mSpeed: Float = 0F
    private var mCount = 0
    var roomList = ArrayList<TrackLocationModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityLocationTrackingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = findViewById(R.id.mapViewTracking)
        mapView.onCreate(savedInstanceState)
        mPreferenceManagerClass = PreferenceManagerClass(this)

        mBackEndLatitude = intent.getDoubleExtra(ConstantsStreetView.OriginLatitude, 0.0)
        mBackEndLongitude = intent.getDoubleExtra(ConstantsStreetView.OriginLongitude, 0.0)

        Log.d(TAG, "onCreate: ==========="+mBackEndLongitude)
        mapViewResult()
        clickListenerTracking()
        setThemeColor()
        initBannerAd()
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
            locationComponent.activateLocationComponent(
                LocationComponentActivationOptions.builder(
                    this,
                    style
                ).build()
            )
            locationComponent.isLocationComponentEnabled = true
            locationComponent.renderMode = RenderMode.NORMAL
        }

        setLocationMarker(LatLng(mBackEndLatitude, mBackEndLongitude), mapboxMap)
    }

    private fun getYourCurrentLocation() {
        val tempMapbox = mapbox
        try {
            myRepositoryStreetView = LocationRepositoryStreetView(this, object :
                MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    if (location != null) {
                        currentLatitude = location.latitude
                        currentLongitude = location.longitude
                        LocationHelperAssistant.setZoomMarker(
                            location.latitude,
                            location.longitude,
                            tempMapbox,
                            zoom
                        )
                        Log.d(TAG, "====speed===LocationMainActivity===" + location.speed)

                        onChangeTrackLocation(location)

                        mTrackingList.add(
                            TrackingLocationModel(
                                currentLatitude,
                                currentLongitude,
                                mSpeedBackend
                            )
                        )
                    } else {
                        myRepositoryStreetView.startLocation()
                    }
                }
            })
        } catch (e: Exception) {
            Log.d(TAG, "Exception$e")
        }

    }

    private fun onChangeTrackLocation(location: Location) {

        mCount++
        mSpeed += location.speed
        mSpeed /= mCount
        //binding.speedLocations.text = DecimalFormat("#.#").format(mSpeed)
        binding.speedLocations.text = mSpeed.toString()

        mapbox.addMarker(
            MarkerOptions().position(
                LatLng(
                    location.latitude,
                    location.longitude
                )
            ).icon(
                IconFactory.getInstance(this)
                    .fromResource(R.drawable.small_marker)
            )
        )

        Log.d(TAG, "onChangeTrackLocation:=============" + mSpeed)

        if (isFirstLocation) {
            currentLatitude = location.latitude
            currentLongitude = location.longitude
            mStartLocation = location
            isFirstLocation = false
        } else {
            mEndLocation = location
            mDistance += LocationHelperAssistant.calculationByDistance(
                LatLng(mStartLocation),
                LatLng(mEndLocation)
            )
            mStartLocation = mEndLocation
            binding.distanceLocations.text = mDistance.toString()

        }
        if (mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Miles, false)) {
            mDistance = mDistance * 0.6214
            binding.distanceLocations.text = DecimalFormat("#.#").format(mDistance)
            binding.speedLocations.text = DecimalFormat("#.#").format(location.speed*0.6214)
            binding.distanceUnit.text = "Miles"
            binding.speedUnit.text = "Miles/h"
        } else {
            binding.distanceLocations.text = DecimalFormat("#.#").format(mDistance)
            binding.speedLocations.text = DecimalFormat("#.#").format(location.speed)
            binding.distanceUnit.text = "Km"
            binding.speedUnit.text = "Km/h"
        }

    }

    private fun clickListenerTracking() {
        binding.toolbarLt.titleTx.text = "Location Tracking"

        val trackFactory = TrackLocationViewModelFactory(this)
        mTrackLocationViewModel =
            ViewModelProvider(this, trackFactory).get(TrackLocationViewModel::class.java)

        binding.showBtn.setOnClickListener {
            startActivity(Intent(this, LocationTrackingListActivity::class.java))
            finish()
        }
        binding.startTrackingLocation.setOnClickListener {
            if (mStart) {
                mTrackingList.clear()
                getYourCurrentLocation()
                mStartTime = getCurrentDateTime(this, 3)
                mStart = false
                binding.locationDataLayout.visibility = View.VISIBLE
                binding.startTrackingLocation.text = "Stop"
            } else {
                myRepositoryStreetView.stopLocation()
                savedTrackingLocations()
                mStart = true
                binding.locationDataLayout.visibility = View.GONE
                binding.startTrackingLocation.text = "Start location"
            }
        }

        binding.toolbarLt.backBtnToolbar.setOnClickListener {
            onBackPressed()
        }

        mTrackLocationViewModel.getAllData().observe(this, androidx.lifecycle.Observer {
            roomList = it as ArrayList<TrackLocationModel>
            Log.d(TAG, "savedTrackingLocations: ==299===Delete=="+roomList.size)
        })
    }
/*
    private fun backGroundServicesLocation() {
        val broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                mSpeedBackend= intent!!.getFloatExtra(ConstantsStreetView.speedLocation, 0.0f)
                mBackEndLatitude= intent!!.getDoubleExtra(ConstantsStreetView.latitudeLocation, 0.0)
                mBackEndLongitude= intent!!.getDoubleExtra(ConstantsStreetView.longitudeLocation, 0.0)

                mTrackingList.add(TrackingLocationModel(mBackEndLatitude,mBackEndLongitude,mSpeedBackend.toString()))
                Log.d(TAG, "=======LocationTrackingMainActivity===" + mSpeedBackend)
                savedTrackingLocations()
            }
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter(MY_TIMER_BROADCAST))

        binding.startTrackingLocation.setOnClickListener {
            val intentService = Intent(this, LocationTrackingService::class.java)
            intentService.action = ACTION_PLAY
            startService(intentService)
        }
        binding.stopBtn.setOnClickListener {
            val intentService = Intent(this, LocationTrackingService::class.java)
            intentService.action = ACTION_STOP
            startService(intentService)
        }
    }
*/

    private fun setLocationMarker(latLng: LatLng, mapboxMap: MapboxMap) {
        LocationHelperAssistant.setZoomMarker(latLng.latitude, latLng.longitude, mapboxMap, zoom)
        // add marker
        if (locationMarker != null) {
            locationMarker!!.remove()
        }
        locationMarker = mapbox.addMarker(
            MarkerOptions().position(LatLng(latLng.latitude, latLng.longitude))
        )
    }

    @DelicateCoroutinesApi
    private fun savedTrackingLocations() {
        try {
            if (mTrackingList != null) {
                val dataString = getCurrentDateTime(this, 5)
                val timeString = getCurrentDateTime(this, 3)

                GlobalScope.launch(Dispatchers.Main) {

                        mTrackLocationViewModel.insertTrackingLocation(
                            TrackLocationModel(
                                id = null,
                                dataString,
                                mStartTime,
                                timeString,
                                mSpeed,
                                mDistance,
                                mTrackingList
                            )
                        )
                        Toast.makeText(
                            this@LocationTrackingMainActivity,
                            "saved",
                            Toast.LENGTH_SHORT
                        ).show()

                    Log.d(TAG, "savedTrackingLocations: =====Delete=="+roomList.size)

                    if (roomList.size>5){
                        mTrackLocationViewModel.deleteTrackingById(roomList[0].id!!)
                        Log.d(TAG, "savedTrackingLocations: ===dete==Delete=="+roomList[0].id!!)
                    }
                }
                Log.d(TAG, "savedTrackingLocations: ===o id==Delete=="+roomList[0].id!!)

            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
        }
    }
/*
    private fun showTRackingData() {
        try {
            preferences =
                getSharedPreferences(ConstantsStreetView.sharedPrefName, Context.MODE_PRIVATE)
            val gson = Gson()
            val jsonString = preferences.getString("mTrackingList", null)
            val type = object : TypeToken<ArrayList<TrackingLocationModel>>() {}.type
            mTrackingList = gson.fromJson(jsonString, type)

            mAdapter = TrackingLocationItemAdapter(mTrackingList, this)
*//*
            binding.locationRecycler.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@LocationTrackingMainActivity)
                adapter = mAdapter
            }*//*
        } catch (e: Exception) {
        }
    }*/

    private fun initBannerAd() {
        LoadAdsStreetViewClock.loadEarthLiveMapBannerAdMob(
            binding.bannerAd.adContainer,
            binding.bannerID,
            this
        )
    }

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.bottomImage.setColorFilter(Color.parseColor(backgroundSecondColor))
        binding.startTrackingLocation.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.showBtn.setBackgroundColor(Color.parseColor(backgroundColor))
    }

    override fun onDestroy() {
        if (mStart == false) {
            savedTrackingLocations()
        }
        super.onDestroy()
    }

}