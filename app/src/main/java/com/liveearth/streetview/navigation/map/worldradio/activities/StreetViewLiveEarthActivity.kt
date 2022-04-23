package com.liveearth.streetview.navigation.map.worldradio.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewLiveEarthBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.*
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions

class StreetViewLiveEarthActivity : BaseStreetViewActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityStreetViewLiveEarthBinding

    val TAG = "StreetViewLiveEarth"
//    private var latitude: Double = 24.748257
//    private var longitude: Double = 67.073477
    private var latitudeTem: Double = 25.1124317
    private var longitudeTrm: Double = 55.138978
    private var latitude: Double = 51.50078
    private var longitude: Double = -0.1245122
    private lateinit var mapView: MapView
    private lateinit var mBuildingPlugin: BuildingPlugin
    lateinit var mapbox: MapboxMap
    private var myStyle: Style? = null
    private var locationMarker: Marker? = null
    var zoom: Int = 16
    var flagMap: Boolean = true
    private lateinit var myRepositoryStreetView: LocationRepositoryStreetView
    var mBottomSheetBehavior: BottomSheetBehavior<View>?=null
    private lateinit var mPreferenceManagerClass:PreferenceManagerClass



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityStreetViewLiveEarthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mPreferenceManagerClass = PreferenceManagerClass(this)

        try {
            latitude = intent.getDoubleExtra(ConstantsStreetView.OriginLatitude,latitude)
            longitude = intent.getDoubleExtra(ConstantsStreetView.OriginLongitude,longitude)

            Log.d(TAG,"------------"+latitude+"=="+longitude)

        } catch (e: Exception) {
        }

        setThemeColor()
        mapViewResult()
        clickListenersLiveEarth()

    }

    private fun clickListenersLiveEarth() {

        mBottomSheetBehavior=BottomSheetBehavior.from(binding.bottomSheet.bottomSheetDrawer)
        mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED

        binding.mapStyleBtn.setOnClickListener {
            if(mBottomSheetBehavior!!.state==BottomSheetBehavior.STATE_EXPANDED){
                mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
            }else {
                mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

    /*    binding.nearByLocations.setOnClickListener {
            val intent = Intent(this,StreetViewNearByPlacesActivity::class.java)
            startActivity(intent)
        }*/

        binding.currentLocationBtn.setOnClickListener {
            getYourCurrentLocation()
        }

        binding.searchLocationEt.setOnClickListener {
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)
            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(ConstantsStreetView.accessToken)
                .build(this)
            startActivityForResult(intent, 1)
        }
/*
        binding.searchLocationEt.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (binding.searchLocationEt.text.toString().isEmpty()) {
                Toast.makeText(this, "Search Box is Empty", Toast.LENGTH_SHORT).show()
                LocationHelper.hideKeyboad(this)
            } else {
                LocationHelper.hideKeyboad(this)
                val obj = CurrentLatLngCoroutine(
                    this,
                    binding.searchLocationEt.text.toString().trim(),
                    object : CurrentLatLngCallback {
                        override fun onSuccessLatLng(latLngs: LatLng?) {
                            latLngs?.let {
                                setLocationMarker(LatLng(it.latitude, it.longitude), mapbox)
                            }
                        }

                        override fun onFailedLatLng(error: String) {
                            Toast.makeText(
                                this@StreetViewLiveEarthActivity,
                                error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
                obj.execute()
            }
            true
        })
*/

        binding.toolbarLt.backLink.setOnClickListener {
            onBackPressed()
        }
    }

    private fun mapViewResult() {
        mapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapbox = mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.setStyle(Style.SATELLITE) { style ->
            myStyle = style
            mBuildingPlugin = BuildingPlugin(mapView, mapboxMap, style)
            mBuildingPlugin.setMinZoomLevel(15f)
        }
//        mapboxMap.uiSettings.logoGravity = Gravity.TOP
//        mapboxMap.uiSettings.attributionGravity = Gravity.TOP
        //LocationHelper.setZoomMarker(latitude, longitude, mapboxMap, 12)

        setLocationMarker(LatLng(latitude, longitude), mapboxMap)
        mapOptionsListener()
    }

    private fun getYourCurrentLocation() {
        val tempMapbox = mapbox
        try {
            myRepositoryStreetView = LocationRepositoryStreetView(this, object : MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        setLocationMarker(LatLng(latitude, longitude), tempMapbox)
                        if (locationMarker != null) {
                            locationMarker!!.remove()
                        }
                        locationMarker = mapbox.addMarker(
                            MarkerOptions().position(LatLng(latitude, longitude)))
                        val latLng = LatLng(location.latitude, location.longitude)
                        StreetViewAddressFromLatLng(
                            this@StreetViewLiveEarthActivity,
                            latLng,
                            object : StreetViewAddressFromLatLng.GeoTaskCallback {
                                override fun onSuccessLocationFetched(fetchedAddress: String?) {
                                    binding.searchLocationEt.text = fetchedAddress
                                }

                                override fun onFailedLocationFetched() {
                                }
                            }).execute()
                        myRepositoryStreetView.stopLocation()
                    } else {
                        myRepositoryStreetView.startLocation()
                    }
                }
            })
            binding.mapLayerLayout.visibility = View.GONE
        } catch (e: Exception) {
            Log.d(TAG, "Exception$e")
        }

    }

    private fun setLocationMarker(latLng: LatLng, mapboxMap: MapboxMap) {
//        latitude = latLng.latitude
//        longitude = latLng.longitude
        LocationHelperAssistant.setZoomMarker(latLng.latitude, latLng.longitude, mapboxMap, zoom)
        Log.d(TAG, "=============" + latLng.latitude)
        // add marker
        if (locationMarker != null) {
            locationMarker!!.remove()
        }
        locationMarker = mapbox.addMarker(
            MarkerOptions().position(LatLng(latLng.latitude, latLng.longitude))
        )
        binding.ImgZoomIn.setOnClickListener {
            if (zoom < 18) {
                zoom++
                LocationHelperAssistant.setZoomMarker(latLng.latitude, latLng.longitude, mapboxMap, zoom)
            }
            binding.mapLayerLayout.visibility = View.GONE
        }

        binding.ImgZoomOut.setOnClickListener {
            if (zoom > 0) {
                zoom--
                LocationHelperAssistant.setZoomMarker(latLng.latitude, latLng.longitude, mapboxMap, zoom)
            }
            binding.mapLayerLayout.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val feature = PlaceAutocomplete.getPlace(data)
                    if (feature != null) {
                        if (feature.center() != null) {
                            if (feature.center()!!.coordinates().isNotEmpty()) {
                                binding.searchLocationEt.text = feature.text()!!
                                latitude = feature.center()?.coordinates()!!.get(1)
                                longitude = feature.center()?.coordinates()!!.get(0)
                                setLocationMarker(
                                    LatLng(feature.center()?.coordinates()!!.get(1), feature.center()?.coordinates()!!.get(0)),
                                    mapbox
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun mapOptionsListener() {

  /*      binding.mapOptionsLt.setOnClickListener{
            if (binding.mapLayerLayout.isVisible){
                binding.mapLayerLayout.visibility = View.GONE
            }else{
                binding.mapLayerLayout.visibility = View.VISIBLE
            }
        }*/
        binding.bottomSheet.trafficStyle.setOnClickListener{
            mapbox.setStyle(Style.TRAFFIC_DAY)
            mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.bottomSheet.satelliteStyle.setOnClickListener{
            mapbox.setStyle(Style.SATELLITE)
            mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.bottomSheet.normalStyle.setOnClickListener{
            mapbox.setStyle(Style.MAPBOX_STREETS)
            mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.bottomSheet.darkStyle.setOnClickListener{
            mapbox.setStyle(Style.DARK)
            mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.threeD.setOnClickListener{
            try {
                if (flagMap){
                    Glide.with(this).load(R.drawable.ic_two_d_icon).into(binding.imageThreeD)
                    flagMap=false
                    mBuildingPlugin.setVisibility(true)
                    LocationHelperAssistant.set3dMap(latitude, longitude,mapbox)
                }else{
                    Glide.with(this).load(R.drawable.ic_three_d_icon).into(binding.imageThreeD)
                    flagMap=true
                    mBuildingPlugin.setVisibility(false)
                    LocationHelperAssistant.setZoomMarker(latitude, longitude,mapbox,zoom)
                }
            } catch (e: Exception) {
            }
            binding.mapLayerLayout.visibility = View.GONE
        }

    }

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.bottomSheet.viewTop.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
        //binding.backFavourite.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.backOne.setColorFilter(Color.parseColor(backgroundColor))
        binding.nearByLocations.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.threeD.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.mapStyleBtn.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.currentLocationBtn.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.ImgZoomIn.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.ImgZoomOut.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.nearByLocations.setCardBackgroundColor(Color.parseColor(backgroundColor))
    }

}