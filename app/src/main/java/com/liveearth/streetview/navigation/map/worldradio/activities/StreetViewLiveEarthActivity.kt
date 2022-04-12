package com.liveearth.streetview.navigation.map.worldradio.activities

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.centurionnavigation.callBack.LiveEarthAddressFromLatLng
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.CurrentLatLngCallback
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.CurrentLatLngCoroutine
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewLiveEarthBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.IconFactory
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
    private var latitude: Double = 24.748257
    private var longitude: Double = 67.073477
    private lateinit var mapView: MapView
    private lateinit var mBuildingPlugin: BuildingPlugin
    lateinit var mapbox: MapboxMap
    private var myStyle: Style? = null
    private var locationMarker: Marker? = null
    var zoom: Int = 16
    var flagMap: Boolean = true
    private lateinit var myRepository: LocationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityStreetViewLiveEarthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        mapViewResult()
        clickListenersLiveEarth()

    }

    private fun clickListenersLiveEarth() {

        binding.nearByLocations.setOnClickListener {
            val intent = Intent(this,StreetViewNearByPlacesActivity::class.java)
            startActivity(intent)
        }


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

        binding.backLink.setOnClickListener {
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

        LocationHelper.setZoomMarker(latitude, longitude, mapboxMap, 12)

        setLocationMarker(LatLng(latitude, longitude), mapboxMap)
        mapOptionsListener()
    }

    private fun getYourCurrentLocation() {
        val tempMapbox = mapbox
        try {
            myRepository = LocationRepository(this, object : MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        setLocationMarker(LatLng(latitude, longitude), tempMapbox)
                        if (locationMarker != null) {
                            locationMarker!!.remove()
                        }
                        /*
                        locationMarker = mapbox.addMarker(
                            MarkerOptions().position(LatLng(latitude, longitude)).icon(
                                IconFactory.getInstance(this@StreetViewLiveEarthActivity)
                                    .fromResource(R.drawable.marker_green_new)
                            )
                        )*/
                        locationMarker = mapbox.addMarker(
                            MarkerOptions().position(LatLng(latitude, longitude)))
                        val latLng = LatLng(location.latitude, location.longitude)
                        LiveEarthAddressFromLatLng(
                            this@StreetViewLiveEarthActivity,
                            latLng,
                            object : LiveEarthAddressFromLatLng.GeoTaskCallback {
                                override fun onSuccessLocationFetched(fetchedAddress: String?) {
                                    binding.searchLocationEt.text = fetchedAddress
                                }

                                override fun onFailedLocationFetched() {
                                }
                            }).execute()
                        myRepository.stopLocation()
                    } else {
                        myRepository.startLocation()
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
        LocationHelper.setZoomMarker(latLng.latitude, latLng.longitude, mapboxMap, zoom)
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
                LocationHelper.setZoomMarker(latLng.latitude, latLng.longitude, mapboxMap, zoom)
            }
            binding.mapLayerLayout.visibility = View.GONE
        }

        binding.ImgZoomOut.setOnClickListener {
            if (zoom > 0) {
                zoom--
                LocationHelper.setZoomMarker(latLng.latitude, latLng.longitude, mapboxMap, zoom)
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
        binding.mapOptionsLt.setOnClickListener{
            if (binding.mapLayerLayout.isVisible){
                binding.mapLayerLayout.visibility = View.GONE
            }else{
                binding.mapLayerLayout.visibility = View.VISIBLE
            }
        }
        binding.trafficMap.setOnClickListener{
            mapbox.setStyle(Style.TRAFFIC_DAY)
            binding.mapLayerLayout.visibility = View.GONE
        }
        binding.satelliteMap.setOnClickListener{
            mapbox.setStyle(Style.SATELLITE)
            binding.mapLayerLayout.visibility = View.GONE
        }
        binding.normalMapStyle.setOnClickListener{
            mapbox.setStyle(Style.MAPBOX_STREETS)
            binding.mapLayerLayout.visibility = View.GONE
        }
        binding.darkMapStyle.setOnClickListener{
            mapbox.setStyle(Style.DARK)
            binding.mapLayerLayout.visibility = View.GONE
        }
        binding.threeD.setOnClickListener{
            try {
                if (flagMap){
                    Glide.with(this).load(R.drawable.ic_two_d_icon).into(binding.imageThreeD)
                    flagMap=false
                    mBuildingPlugin.setVisibility(true)
                    LocationHelper.set3dMap(latitude, longitude,mapbox)
                }else{
                    Glide.with(this).load(R.drawable.ic_three_d_icon).into(binding.imageThreeD)
                    flagMap=true
                    mBuildingPlugin.setVisibility(false)
                    LocationHelper.setZoomMarker(latitude, longitude,mapbox,zoom)
                }
            } catch (e: Exception) {
            }
            binding.mapLayerLayout.visibility = View.GONE
        }

    }



}