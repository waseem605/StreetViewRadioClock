package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.centurionnavigation.callBack.LiveEarthAddressFromLatLng
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewLocationAPIServices
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewNearByCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.StreetViewNearMeCallBack
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.Result
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.StreetViewNearPlacesModel
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewNearByPlacesBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.NearMeLocationsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.NearLocationsModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.building.BuildingPlugin

class StreetViewNearByPlacesActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityStreetViewNearByPlacesBinding
    val TAG = "StreetViewNearBy"
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var mapView: MapView
    private lateinit var mBuildingPlugin: BuildingPlugin
    lateinit var mapbox: MapboxMap
    private var myStyle: Style? = null
    private var mLocationMarker: Marker? = null
    private var nearLocationMarker: Marker? = null
    private var mOrigin: Point? = null
    private var mDestination: Point? = null
    var zoom: Int = 16
    var flagMap: Boolean = true
    private var mLocationsList: ArrayList<NearLocationsModel> = ArrayList()
    private lateinit var myRepository: LocationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityStreetViewNearByPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = findViewById(R.id.mapViewNearMe)
        mapView.onCreate(savedInstanceState)
        mapViewResult()


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
        mapboxMap.uiSettings.logoGravity = Gravity.TOP
        mapboxMap.uiSettings.attributionGravity = Gravity.TOP

        getYourCurrentLocation()

        binding.meetMeSearchBtn.setOnClickListener {
            searchNearLocation()
        }
    }

    private fun searchNearLocation() {
        val apiLocationServices = StreetViewLocationAPIServices(object : StreetViewNearByCallBack {
            override fun onSuccess(data: StreetViewNearPlacesModel) {
                val nearLocationData = data.results

                if (nearLocationMarker != null) {
                    nearLocationMarker!!.remove()
                }
                try {
                    for (i in 0 until nearLocationData.size) {
                        nearLocationMarker = mapbox.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    nearLocationData[i].geocodes.main.latitude,
                                    nearLocationData[i].geocodes.main.longitude
                                )
                            )
                        )
                        //clickListenerOnMarker(nearLocationData[i].geocodes.main.latitude,nearLocationData[i].geocodes.main.longitude,nearLocationData[i].name)
                    }
                    showNearLocationsRecycler(data.results as ArrayList<Result>)
                } catch (e: Exception) {
                }

            }

            override fun onFailure(userError: String) {
                Toast.makeText(
                    this@StreetViewNearByPlacesActivity,
                    "==Failure $userError",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onLoading(loading: Boolean) {
            }

        })
        val ll = latitude.toString() + "," + longitude.toString()
        val category = "13000"
        apiLocationServices.getDataServiceBySearchId(ll, category)
    }

    private fun showNearLocationsRecycler(nearLocationData: ArrayList<Result>) {
        //binding.meetMeSearchBtn.visibility = View.GONE
        val adapterLocations = NearMeLocationsAdapter(nearLocationData, this, object : StreetViewNearMeCallBack {
            override fun onLocationInfo(model: Result) {

                val intent =
                    Intent(this@StreetViewNearByPlacesActivity, StreetViewRouteActivity::class.java)
                intent.putExtra(ConstantsStreetView.OriginLatitude, latitude)
                intent.putExtra(ConstantsStreetView.OriginLongitude, longitude)
                intent.putExtra(ConstantsStreetView.DestinationLatitude, model.geocodes.main.latitude)
                intent.putExtra(ConstantsStreetView.DestinationLongitude, model.geocodes.main.longitude)
                startActivity(intent)

            }

            override fun shareLocation(model: Result) {

            }

            override fun addToFavouriteLocation(model: Result) {
            }

            override fun onClickOfItemLocation(model: Result, pos: Int) {

            }

        })

        binding.locationRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@StreetViewNearByPlacesActivity,
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = adapterLocations
        }

    }


    private fun getYourCurrentLocation() {
        val tempMapbox = mapbox
        try {
            myRepository = LocationRepository(this, object : MyLocationListener {
                override fun onLocationChanged(location: Location) {
                    location.let {
                        setLocationMarker(LatLng(it.latitude, it.longitude), tempMapbox)
                        if (mLocationMarker != null) {
                            mLocationMarker!!.remove()
                        }
                        mLocationMarker = mapbox.addMarker(
                            MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        )
                        val latLng = LatLng(it.latitude, it.longitude)
                        LiveEarthAddressFromLatLng(this@StreetViewNearByPlacesActivity,
                            latLng,
                            object : LiveEarthAddressFromLatLng.GeoTaskCallback {
                                override fun onSuccessLocationFetched(fetchedAddress: String?) {
                                    binding.searchCurrentLocation.setText(fetchedAddress)
                                }

                                override fun onFailedLocationFetched() {
                                }
                            }).execute()
                        myRepository.stopLocation()
                    }
                }
            })
        } catch (e: Exception) {
            Log.d(TAG, "Exception$e")
        }

    }

    private fun setLocationMarker(latLng: LatLng, mapboxMap: MapboxMap) {
        latitude = latLng.latitude
        longitude = latLng.longitude
        LocationHelper.setZoomMarker(latitude, longitude, mapboxMap, zoom)
        // add marker
        if (mLocationMarker != null) {
            mLocationMarker!!.remove()
        }
        mLocationMarker = mapbox.addMarker(
            MarkerOptions().position(LatLng(latitude, longitude))
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