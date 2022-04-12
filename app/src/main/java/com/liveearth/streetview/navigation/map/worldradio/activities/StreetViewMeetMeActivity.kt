package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.centurionnavigation.callBack.LiveEarthAddressFromLatLng
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewLocationAPIServices
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewNearByCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.StreetViewNearMeCallBack
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewMeetMeBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.MeetMeLocationsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.NearMeLocationsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.Result
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.StreetViewNearPlacesModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
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
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import com.streetview.map.navigation.live.earthmap.utils.StreetViewGeocoderFromAddress
import java.text.DecimalFormat

@SuppressLint("LogNotTimber")
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
    private var middleLocationMarker: Marker? = null
    private var mLocationMarkerDestin: Marker? = null
    private var mOrigin: Point? = null
    private var mDestination: Point? = null
    var zoom: Int = 16
    var destinationFlag: Boolean = false
    private lateinit var midLaLong: LatLng
    private lateinit var myRepository: LocationRepository
    lateinit var animationDown: Animation
    lateinit var animationDownToUp: Animation
    lateinit var animationTopToDown: Animation
    lateinit var animationPointToDown: Animation


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

        binding.toolbar.titleTx.text = "Meet Me Middle"
        animationTopToDown = AnimationUtils.loadAnimation(this, R.anim.slide_top_to_down)
        animationDownToUp = AnimationUtils.loadAnimation(this, R.anim.slide_point_up)
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down_to_point)
        animationPointToDown = AnimationUtils.loadAnimation(this, R.anim.slide_point_to_down)

        binding.topLayout.startAnimation(animationTopToDown)

        binding.toolbar.backLink.setOnClickListener {
            onBackPressed()
        }

        binding.cMeetMeBtn.setOnClickListener {
            getCurrentLocationUser()
        }

        binding.voiceDestination.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            startActivityForResult(intent, 302)
        }

        binding.meetMeSearchBtn.setOnClickListener {
            streetViewRouteData()
        }

        binding.meetNavigateBtn.setOnClickListener {
            val intent = Intent(this@StreetViewMeetMeActivity, StreetViewRouteActivity::class.java)
            intent.putExtra(ConstantsStreetView.OriginLatitude, mLatitude)
            intent.putExtra(ConstantsStreetView.OriginLongitude, mLongitude)
            intent.putExtra(ConstantsStreetView.DestinationLatitude, mLatitudeDest)
            intent.putExtra(ConstantsStreetView.DestinationLongitude, mLongitudeDest)
            startActivity(intent)
        }

        binding.searchCurrentLocation.setOnClickListener {
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)
            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(ConstantsStreetView.accessToken)
                .build(this)
            startActivityForResult(intent, 1)
        }

        binding.searchLocationDestination.setOnClickListener {
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)
            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(ConstantsStreetView.accessToken)
                .build(this)
            startActivityForResult(intent, 2)
        }
/*

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
*/

    }

    private fun streetViewRouteData() {
        binding.locationRecycler.visibility = View.GONE
        if (mLatitudeDest == 0.0 && mLongitudeDest == 0.0 && binding.searchLocationDestination.text.toString() != "") {
            Toast.makeText(this, "Please enter your destination address", Toast.LENGTH_SHORT).show()
        } else if (binding.searchLocationDestination.text.toString() == "") {
            Toast.makeText(this, "your destination not find", Toast.LENGTH_SHORT).show()
        } else {
            nearMeLocationsMidPoints()
        }

    }

    //find near me locations hit
    private fun nearMeLocationsMidPoints() {
        try {
            midLaLong = LocationHelper.midPointLocation(
                mLatitude,
                mLongitude,
                mLatitudeDest,
                mLongitudeDest
            )
            midLaLong.let {
                LiveEarthAddressFromLatLng(
                    this,
                    it,
                    object : LiveEarthAddressFromLatLng.GeoTaskCallback {
                        override fun onSuccessLocationFetched(fetchedAddress: String?) {
                            binding.middleAddressTx.text = fetchedAddress
                            Log.d(
                                TAG,
                                "onSuccessLocationFetched: =========${midLaLong.latitude}============$fetchedAddress"
                            )
                        }

                        override fun onFailedLocationFetched() {
                            Log.d(TAG, "onFailedLocationFetched: fetchedAddress error")
                        }

                    }).execute()
                binding.distanceCardInfo.visibility = View.VISIBLE
                binding.distanceCardInfo.startAnimation(animationDownToUp)

                LocationHelper.setZoomMarker(it.latitude, it.longitude, mapbox, zoom)
                val distance =
                    LocationHelper.calculationByDistance(LatLng(mLatitude, mLongitude), midLaLong)
                binding.distanceKm.text = "${DecimalFormat("#.#").format(distance)} Km"
                // add marker
                if (middleLocationMarker != null) {
                    middleLocationMarker!!.remove()
                }
                middleLocationMarker = mapbox.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                )



                binding.nearMeSearchBtn.setOnClickListener {
                    nearMeLocationsAll()
                }
            } ?: kotlin.run {
                Toast.makeText(this, "your location is not find", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.d(TAG, "---------nearMeLocationsMidPoints: ${e.message}")
        }

    }

    //places all
    private fun nearMeLocationsAll() {
        val locationServiceApi = StreetViewLocationAPIServices(object : StreetViewNearByCallBack {
            override fun onSuccess(data: StreetViewNearPlacesModel) {
                val nearMeLocationData = data.results
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.topLayout.visibility = View.GONE
                    binding.distanceCardInfo.visibility = View.GONE
                    binding.locationRecycler.visibility = View.VISIBLE
                    binding.locationRecycler.startAnimation(animationDownToUp)
                }, 500)
                binding.topLayout.startAnimation(animationDown)
                binding.distanceCardInfo.startAnimation(animationPointToDown)


                if (mOtherLocationMarker != null) {
                    mOtherLocationMarker!!.remove()
                }
                try {

                    LocationHelper.setZoomMarker(
                        midLaLong.latitude,
                        midLaLong.longitude,
                        mapbox,
                        10
                    )

                    for (i in 0 until nearMeLocationData.size) {
                        onClickNearMeMarkers(
                            nearMeLocationData[i].geocodes.main.latitude,
                            nearMeLocationData[i].geocodes.main.longitude,
                            nearMeLocationData[i].name
                        )
                    }
                    showNearLocationsRecycler(data.results as ArrayList<Result>)
                } catch (e: Exception) {
                }
            }

            override fun onFailure(userError: String) {
                Toast.makeText(
                    this@StreetViewMeetMeActivity,
                    "==Failure $userError",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onLoading(loading: Boolean) {

            }
        })

        val locationString = midLaLong.latitude.toString() + "," + midLaLong.longitude.toString()
        val category = "13000"
        locationServiceApi.getDataServiceBySearchId(locationString, category)

    }

    private fun onClickNearMeMarkers(latitude: Double, longitude: Double, name: String) {
        mOtherLocationMarker = mapbox.addMarker(
            MarkerOptions().position(
                LatLng(
                    latitude,
                    longitude
                )
            ).icon(
                IconFactory.getInstance(this@StreetViewMeetMeActivity)
                    .fromResource(R.drawable.marker_black)
            ).title(name)
        )

        mapbox.setOnMarkerClickListener(object : MapboxMap.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker): Boolean {
                try {
                    binding.distanceCardInfo.visibility = View.VISIBLE
                    binding.middleAddressTx.text = marker.title
                    binding.nearMeSearchBtn.text = "Share"
                    var distance =
                        (marker.position.distanceTo(LatLng(mLatitude, mLongitude)) / 1000)
                    binding.distanceKm.text = distance.toString()


                } catch (e: Exception) {
                }

                return true
            }

        })

    }

    private fun setMarkerDestination(latLng: LatLng, mapbox: MapboxMap) {
        mLatitudeDest = latLng.latitude
        mLongitudeDest = latLng.longitude
        LocationHelper.setZoomMarker(mLatitudeDest, mLongitudeDest, mapbox, zoom)
        // add marker
        if (mOtherLocationMarker != null) {
            mOtherLocationMarker!!.remove()
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
                        setLocationMarker(LatLng(it.latitude, it.longitude), tempMapbox)
                        /*      if (mLocationMarker != null) {
                                  mLocationMarker !!.remove()
                              }
                              mLocationMarker = mapbox.addMarker(
                                  MarkerOptions().position(LatLng(mLatitude, mLongitude))
                              )*/
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
                    } ?: kotlin.run {
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
            mLocationMarker!!.remove()
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


    private fun showNearLocationsRecycler(nearLocationData: ArrayList<Result>) {
        val adapterLocations =
            MeetMeLocationsAdapter(nearLocationData, this, object : StreetViewNearMeCallBack {
                override fun onLocationInfo(model: Result) {
                    val intent =
                        Intent(this@StreetViewMeetMeActivity, StreetViewRouteActivity::class.java)
                    /*        intent.putExtra(ConstantsStreetView.OriginLatitude, latitude)
                            intent.putExtra(ConstantsStreetView.OriginLongitude, longitude)
                            intent.putExtra(ConstantsStreetView.DestinationLatitude, model.geocodes.main.latitude)
                            intent.putExtra(ConstantsStreetView.DestinationLongitude, model.geocodes.main.longitude)
                            startActivity(intent)*/
                }

                override fun shareLocation(model: Result) {
                    Log.d(TAG, "shareLocation: +++++++++++")
                    LocationHelper.shareLocation(
                        this@StreetViewMeetMeActivity,
                        model.geocodes.main.latitude,
                        model.geocodes.main.longitude
                    )

                }

                override fun addToFavouriteLocation(model: Result) {
                    Log.d(TAG, "addToFavouriteLocation: --+-+-+-+-+-")
                }

                override fun onClickOfItemLocation(model: Result, pos: Int) {

                }

            })

        binding.locationRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@StreetViewMeetMeActivity,
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = adapterLocations
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
                                mLatitude = feature.center()?.coordinates()!!.get(1)
                                mLongitude = feature.center()?.coordinates()!!.get(0)
                                binding.searchCurrentLocation.text = feature.text()!!
                                setLocationMarker(
                                    LatLng(mLatitude, mLongitude),
                                    mapbox
                                )
                            }
                        }
                    }
                }
            }
            2 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val feature = PlaceAutocomplete.getPlace(data)
                    if (feature != null) {
                        if (feature.center() != null) {
                            if (feature.center()!!.coordinates().isNotEmpty()) {
                                mLatitudeDest = feature.center()?.coordinates()!!.get(1)
                                mLongitudeDest = feature.center()?.coordinates()!!.get(0)
                                binding.searchLocationDestination.text = feature.text()!!
                                setLocationMarkerTwo(
                                    LatLng(mLatitudeDest, mLongitudeDest),
                                    mapbox
                                )

                            }
                        }
                    }
                }
            }
            302 -> {
                if (requestCode == 302 && resultCode == Activity.RESULT_OK) {
                    val arrayList: ArrayList<String> =
                        data !!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                    val voiceText: String = arrayList.get(0)
                    latLongFromAddressDestination(voiceText, requestCode)
                    Log.d("onActivityResult", "onActivityResult  voiceText: $voiceText")
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun latLongFromAddressDestination(voiceText: String, requestCode: Int) {

        if (voiceText !=""){
            Log.d("onActivityResult", "onActivityResult  voiceText====: --")

            StreetViewGeocoderFromAddress(this,voiceText,object : StreetViewGeocoderFromAddress.GeoTaskCallback{
                override fun onSuccessLocationFetched(fetchedLatLngs: LatLng?) {

                    Log.d("onActivityResult", "onActivityResult  voiceText====: ${fetchedLatLngs!!.latitude}")
                    binding.searchLocationDestination.text = voiceText
                    //binding.searchLocationDestination.setText(voiceText)
                    setLocationMarkerTwo(fetchedLatLngs,mapbox)


                   /* when (requestCode){
                        302->{
                            binding.searchLocationDestination.text = voiceText
                            //binding.searchLocationDestination.setText(voiceText)
                            setLocationMarkerTwo(fetchedLatLngs,mapbox)
                        }
                    }*/
                }

                override fun onFailedLocationFetched() {
                    Log.d("onActivityResult", "onActivityResult  error:")
                }

            }).execute()
        }
    }

    private fun setLocationMarkerTwo(latLng: LatLng, mapbox: MapboxMap) {
        mLatitudeDest = latLng.latitude
        mLongitudeDest = latLng.longitude
        LocationHelper.setZoomMarker(mLatitudeDest, mLongitudeDest, mapbox, zoom)
        // add marker
        if (mLocationMarkerDestin != null) {
            mLocationMarkerDestin!!.remove()
        }
        mLocationMarkerDestin = mapbox.addMarker(
            MarkerOptions().position(LatLng(mLatitudeDest, mLongitudeDest))
        )
    }


}