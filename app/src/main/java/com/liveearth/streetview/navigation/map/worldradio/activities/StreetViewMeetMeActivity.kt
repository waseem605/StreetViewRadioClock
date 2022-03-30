package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.centurionnavigation.callBack.LiveEarthAddressFromLatLng
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.CurrentLatLngCallback
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.CurrentLatLngCoroutine
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewLocationAPIServices
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewNearByCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.StreetViewNearPlacesModel
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewMeetMeBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.mapbox.geojson.Point
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

class StreetViewMeetMeActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityStreetViewMeetMeBinding
    val TAG = "StreetViewMeetMe"
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    private var mLatitudeDest: Double = 0.0
    private var mLongitudeDest: Double = 0.0
    private lateinit var mapView: MapView
    private lateinit var mBuildingPlugin: BuildingPlugin
    lateinit var mapbox: MapboxMap
    private var myStyle: Style? = null
    private var mLocationMarker: Marker? = null
    private var mOtherLocationMarker: Marker? = null
    private var middleLocationMarker: Marker? =null
    private var mOrigin: Point? = null
    private var mDestination: Point? = null
    var zoom: Int = 16
    var flagMap: Boolean = true
    private lateinit var midLaLong:LatLng
    private lateinit var myRepository: LocationRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityStreetViewMeetMeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = findViewById(R.id.mapViewMeetMe)
        mapView.onCreate(savedInstanceState)
        mapViewResultMeetMe()
        meetMeClickListener()

    }

    private fun meetMeClickListener() {

        binding.cMeetMeBtn.setOnClickListener {
            getCurrentLocationUser()
        }

        binding.meetMeSearchBtn.setOnClickListener {
            streetViewRouteData()
        }

        binding.searchLocationDestination.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (binding.searchLocationDestination.text.toString().isEmpty()) {
                Toast.makeText(this, "Search Box is Empty", Toast.LENGTH_SHORT).show()
                LocationHelper.hideKeyboad(this)
            } else {
                LocationHelper.hideKeyboad(this)
                val obj = CurrentLatLngCoroutine(
                    this,
                    binding.searchLocationDestination.text.toString().trim(),
                    object : CurrentLatLngCallback {
                        override fun onSuccessLatLng(latLngs: LatLng?) {
                            latLngs?.let {
                                mLatitudeDest = it.latitude
                                mLongitudeDest = it.longitude
                                setMarkerDestination(LatLng(it.latitude, it.longitude), mapbox)
                            }
                        }

                        override fun onFailedLatLng(error: String) {
                            Toast.makeText(this@StreetViewMeetMeActivity,error, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                obj.execute()
            }
            true
        })

    }

    private fun streetViewRouteData() {
        if (mLatitudeDest ==0.0 && mLongitudeDest==0.0 && binding.searchLocationDestination.text.toString() !=""){
            Toast.makeText(this,"Please enter your destination address",Toast.LENGTH_SHORT).show()
        }else if(binding.searchLocationDestination.text.toString() ==""){
            Toast.makeText(this,"your destination not find",Toast.LENGTH_SHORT).show()
        }else{
            nearMeLocationsMidPoints()
        }

    }

    //find near me locations hit
    private fun nearMeLocationsMidPoints() {
        midLaLong = LocationHelper.midPointLocation(mLatitude,mLongitude,mLatitudeDest,mLongitudeDest)
        midLaLong?.let {
            binding.distanceCardInfo.visibility = View.VISIBLE
            LocationHelper.setZoomMarker(it.latitude, it.longitude, mapbox, zoom)
            binding.distanceKm.text = LocationHelper.calculationByDistance(LatLng(mLatitude,mLongitude),midLaLong).toString()
            // add marker
            if (middleLocationMarker != null) {
                middleLocationMarker !!.remove()
            }
            middleLocationMarker = mapbox.addMarker(
                MarkerOptions().position(LatLng(it.latitude, it.longitude))
            )

            binding.nearMeSearchBtn.setOnClickListener {
                nearMeLocationsAll()
            }
        }?: kotlin.run {
            Toast.makeText(this,"your location is not find",Toast.LENGTH_SHORT).show()
        }
    }

    //places all
    private fun nearMeLocationsAll() {
        val locationServiceApi = StreetViewLocationAPIServices(object :StreetViewNearByCallBack{
            override fun onSuccess(data: StreetViewNearPlacesModel) {
                val nearMeLocationData=data.results
                binding.distanceCardInfo.visibility = View.GONE
                if (mOtherLocationMarker != null) {
                    mOtherLocationMarker!!.remove()
                }
                try {
                    for (i in 0 until nearMeLocationData.size) {
                        onClickNearMeMarkers( nearMeLocationData[i].geocodes.main.latitude,nearMeLocationData[i].geocodes.main.longitude,nearMeLocationData[i].name)
                    }
                } catch (e: Exception) {
                }
            }

            override fun onFailure(userError: String) {
                Toast.makeText(this@StreetViewMeetMeActivity,"==Failure $userError",Toast.LENGTH_SHORT).show()
            }

            override fun onLoading(loading: Boolean) {

            }
        })

        val locationString= midLaLong.latitude.toString()+","+midLaLong.longitude.toString()
        val category = "13000"
          locationServiceApi.getDataServiceBySearchId(locationString,category)

    }

    private fun onClickNearMeMarkers(latitude: Double, longitude: Double, name: String) {
        mOtherLocationMarker = mapbox.addMarker(
            MarkerOptions().position(
                LatLng(
                    latitude,
                    longitude
                )
            ).icon(IconFactory.getInstance(this@StreetViewMeetMeActivity).fromResource(R.drawable.marker_black)).title(name)
        )

        mapbox.setOnMarkerClickListener(object :MapboxMap.OnMarkerClickListener{
            override fun onMarkerClick(marker: Marker): Boolean {
                try {
                    binding.distanceCardInfo.visibility = View.VISIBLE
                    binding.middleAddressTx.text = marker.title
                    binding.nearMeSearchBtn.text = "Share"
                    var distance = (marker.position.distanceTo(LatLng(mLatitude, mLongitude))/1000)
                    binding.distanceKm.text = distance.toString()


                    binding.meetNavigateBtn.setOnClickListener {
                        val intent = Intent(this@StreetViewMeetMeActivity, StreetViewRouteActivity::class.java)
                        intent.putExtra(ConstantsStreetView.OriginLatitude, mLatitude)
                        intent.putExtra(ConstantsStreetView.OriginLongitude, mLongitude)
                        intent.putExtra(ConstantsStreetView.DestinationLatitude, marker.position.latitude)
                        intent.putExtra(ConstantsStreetView.DestinationLongitude, marker.position.longitude)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                }

                return true
            }

        })

    }

    private fun setMarkerDestination(latLng: LatLng, mapbox: MapboxMap) {
        mLatitudeDest= latLng.latitude
        mLongitudeDest = latLng.longitude
        LocationHelper.setZoomMarker(mLatitudeDest, mLongitudeDest, mapbox, zoom)
        // add marker
        if (mOtherLocationMarker != null) {
            mOtherLocationMarker !!.remove()
        }
        mOtherLocationMarker = mapbox.addMarker(
            MarkerOptions().position(LatLng(mLatitudeDest, mLongitudeDest))
        )
    }

    private fun mapViewResultMeetMe() {
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
        mapboxMap.uiSettings.logoGravity = Gravity.TOP
        mapboxMap.uiSettings.attributionGravity = Gravity.TOP

        getCurrentLocationUser()

    }


    private fun getCurrentLocationUser() {
        val tempMapbox = mapbox
        try {
            myRepository = LocationRepository(this, object : MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    location?.let {
                        mLatitude = it.latitude
                        mLongitude = it.longitude
                        setLocationMarker(LatLng( it.latitude, it.longitude), tempMapbox)
                        if (mLocationMarker != null) {
                            mLocationMarker !!.remove()
                        }
                        mLocationMarker = mapbox.addMarker(
                            MarkerOptions().position(LatLng(mLatitude, mLongitude))
                        )
                        val latLng = LatLng(it.latitude, it.longitude)
                        LiveEarthAddressFromLatLng(this@StreetViewMeetMeActivity,
                            latLng,
                            object : LiveEarthAddressFromLatLng.GeoTaskCallback {
                                override fun onSuccessLocationFetched(fetchedAddress: String?) {
                                    binding.searchCurrentLocation.setText(fetchedAddress)
                                }

                                override fun onFailedLocationFetched() {
                                }
                            }).execute()
                        myRepository.stopLocation()
                    }?: kotlin.run {
                        myRepository.startLocation()
                    }
                }
            })
        } catch (e: Exception) {
            Log.d(TAG, "Exception$e")
        }

    }

    private fun setLocationMarker(latLng: LatLng, mapboxMap: MapboxMap) {

        mLatitude = latLng.latitude
        mLongitude = latLng.longitude
        LocationHelper.setZoomMarker(mLatitude, mLongitude, mapboxMap, zoom)
        // add marker
        if (mLocationMarker != null) {
            mLocationMarker !!.remove()
        }
        mLocationMarker = mapbox.addMarker(
            MarkerOptions().position(LatLng(mLatitude, mLongitude))
        )

        /*binding.ImgZoomIn.setOnClickListener {
            if (zoom<18) {
                zoom++
                LocationHelper.setZoomMarker(latitude, longitude, mapboxMap, zoom)
            }
            binding.mapLayerLayout.visibility = View.GONE
        }

        binding.ImgZoomOut.setOnClickListener {
            if (zoom>0) {
                zoom--
                LocationHelper.setZoomMarker(latitude, longitude, mapboxMap, zoom)
            }
            binding.mapLayerLayout.visibility = View.GONE
        }
        */
    }

}