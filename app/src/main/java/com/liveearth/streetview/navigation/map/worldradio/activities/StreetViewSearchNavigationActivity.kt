package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.LoadAdsStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewSearchNavigationBinding
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


@SuppressLint("LogNotTimber")
class StreetViewSearchNavigationActivity : BaseStreetViewActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityStreetViewSearchNavigationBinding
    private lateinit var mPreferenceManagerClass:PreferenceManagerClass
    val TAG = "SearchNavigation"
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    private var mLatitudeTwo: Double = 0.0
    private var mLongitudeTwo: Double = 0.0
    private var mLatitudeThree: Double = 0.0
    private var mLongitudeThree: Double = 0.0
    private var mLatitudeDestination: Double = 0.0
    private var mLongitudeDestination: Double = 0.0
    private lateinit var mMapView: MapView
    private lateinit var mBuildingPlugin: BuildingPlugin
    lateinit var mapbox: MapboxMap
    private var myStyle: Style? = null
    private var mLocationMarker: Marker? = null
    private var mLocationMarkerTwo: Marker? = null
    private var mLocationMarkerThree: Marker? = null
    private var mLocationMarkerDestination: Marker? = null
    var zoom: Int = 16
    private var customRoutList: ArrayList<LatLng> = ArrayList()
    private lateinit var myRepositoryStreetView: LocationRepositoryStreetView
    var mBottomSheetBehavior: BottomSheetBehavior<View>?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityStreetViewSearchNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mMapView = findViewById(R.id.mapViewMainNavigation)

        mPreferenceManagerClass = PreferenceManagerClass(this)
        searchNavigationClicks()
        mapViewResultAsync()
        setThemeColor()
        initBannerAd()

    }



    private fun searchNavigationClicks() {

        mBottomSheetBehavior=BottomSheetBehavior.from(binding.bottomSheet.bottomSheetDrawer)
        mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED

        binding.mapStyleOption.setOnClickListener {
            if(mBottomSheetBehavior!!.state==BottomSheetBehavior.STATE_EXPANDED){
                mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
            }else {
                mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        binding.addMoreLocation.setOnClickListener {
            addMoreLocations()
        }
        binding.searchLayout.setOnClickListener {
        }

        binding.currentLocationImage.setOnClickListener {
            userCurrentLocation()
        }

        binding.locationOne.setOnClickListener {
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)
            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(ConstantsStreetView.accessToken)
                .build(this)
            startActivityForResult(intent, 1)
        }

        binding.locationTwo.setOnClickListener {
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)
            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(ConstantsStreetView.accessToken)
                .build(this)
            startActivityForResult(intent, 2)
        }
        binding.locationThree.setOnClickListener {
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)
            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(ConstantsStreetView.accessToken)
                .build(this)
            startActivityForResult(intent, 3)
        }

        binding.locationDestination.setOnClickListener {
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)
            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(ConstantsStreetView.accessToken)
                .build(this)
            startActivityForResult(intent, 4)
        }

        binding.navigationBtn.setOnClickListener {
            navigateRoute()
        }

        binding.voiceTwoLocation.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            startActivityForResult(intent, 302)
        }
        binding.voiceThreeLocation.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            startActivityForResult(intent, 303)
        }
        binding.voiceDestinationBtn.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            startActivityForResult(intent, 304)
        }

        binding.backLink.setOnClickListener {
            onBackPressed()
        }

        designConditions()

    }

    private fun addMoreLocations() {
        binding.locationTwoConst.visibility = View.VISIBLE
        binding.img2.visibility = View.VISIBLE
        binding.img3.visibility = View.VISIBLE

        if (mLatitudeTwo !=0.0) {
            binding.locationThreeConst.visibility = View.VISIBLE
            binding.img4.visibility = View.VISIBLE
            binding.img5.visibility = View.VISIBLE
        }else{
            setToast(this,"Please select your second point")
        }



    }

    private fun designConditions() {
        if (binding.locationThreeConst.isVisible){
            Log.d(TAG,"=========================212")
            binding.img5.visibility = View.VISIBLE
            binding.img4.visibility = View.VISIBLE
        }else{
            Log.d(TAG,"=========================else")
            binding.img5.visibility = View.GONE
            binding.img4.visibility = View.GONE
        }

        if (binding.locationTwoConst.isVisible){
            Log.d(TAG,"=========================212")
            binding.img2.visibility = View.VISIBLE
            binding.img3.visibility = View.VISIBLE
        }else{
            Log.d(TAG,"=========================else")
            binding.img2.visibility = View.GONE
            binding.img3.visibility = View.GONE
        }
    }

    private fun navigateRoute() {
        if (mLocationMarker == null) {
            setToast(this, "Please select your starting Location")
        } else if (mLocationMarkerDestination == null) {
            setToast(this, "Please select your Destination Location")
        } else if (mLocationMarkerTwo != null || mLocationMarkerThree != null) {

            customRoutList.add(LatLng(mLatitude, mLongitude))
            if (mLocationMarkerTwo != null){
                customRoutList.add(
                    LatLng(mLatitudeTwo, mLongitudeTwo))
            }
            if (mLocationMarkerThree != null){
                customRoutList.add(
                    LatLng(mLatitudeThree, mLongitudeThree))
            }

            customRoutList.add(LatLng(mLatitudeDestination, mLongitudeDestination))

            for (i in 0 until customRoutList.size){
                Log.d(TAG, "*******************: ===== ${customRoutList[i].latitude} ====== ${customRoutList[i].longitude} ====")
            }

            val intentRoute = Intent(this, StreetViewRouteActivity::class.java)
            intentRoute.putParcelableArrayListExtra(ConstantsStreetView.MultiPointsRouteList, customRoutList)
            intentRoute.putExtra(ConstantsStreetView.OriginLatitude, mLatitude)
            intentRoute.putExtra(ConstantsStreetView.OriginLongitude, mLongitude)
            intentRoute.putExtra(ConstantsStreetView.DestinationLatitude, mLatitudeDestination)
            intentRoute.putExtra(ConstantsStreetView.DestinationLongitude, mLongitudeDestination)
            intentRoute.putExtra(ConstantsStreetView.MultiPointsRoute,true)
            startActivity(intentRoute)
        } else {
            val intent = Intent(this, StreetViewRouteActivity::class.java)
            intent.putExtra(ConstantsStreetView.OriginLatitude, mLatitude)
            intent.putExtra(ConstantsStreetView.OriginLongitude, mLongitude)
            intent.putExtra(ConstantsStreetView.MultiPointsRoute,false)
            intent.putExtra(ConstantsStreetView.DestinationLatitude, mLatitudeDestination)
            intent.putExtra(ConstantsStreetView.DestinationLongitude, mLongitudeDestination)
            startActivity(intent)
        }
    }

    private fun userCurrentLocation() {
        myRepositoryStreetView = LocationRepositoryStreetView(this, object : MyLocationListener {
            override fun onLocationChanged(location: Location) {
                location.let {
                    myRepositoryStreetView.stopLocation()
                    mLatitude = it.latitude
                    mLongitude = it.longitude

                    if (mLocationMarker != null) {
                        mLocationMarker!!.remove()
                    }
                    mLocationMarker = mapbox.addMarker(
                        MarkerOptions().position(LatLng(it.latitude, it.longitude))
                    )
                    //Zoom marker
                    LocationHelperAssistant.setZoomMarker(it.latitude, it.longitude, mapbox, zoom)
                    //location name from lat long
                    StreetViewAddressFromLatLng(this@StreetViewSearchNavigationActivity,
                        LatLng(it.latitude, it.longitude),
                        object : StreetViewAddressFromLatLng.GeoTaskCallback {
                            override fun onSuccessLocationFetched(fetchedAddress: String?) {
                                binding.locationOne.text = fetchedAddress
                            }

                            override fun onFailedLocationFetched() {
                            }
                        }).execute()

                }
            }
        })
    }

    private fun mapViewResultAsync() {
        mMapView.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapbox = mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.setStyle(Style.MAPBOX_STREETS) { style ->
            myStyle = style
            mBuildingPlugin = BuildingPlugin(mMapView, mapboxMap, style)
            mBuildingPlugin.setMinZoomLevel(15f)
        }
        //mapboxMap.uiSettings.logoMarginTop =
        mapboxMap.uiSettings.logoGravity = Gravity.TOP
        mapboxMap.uiSettings.attributionGravity = Gravity.TOP

        userCurrentLocation()
        mapStyleOptionClickListener()
    }

    private fun setLocationMarker(latLng: LatLng, mapbox: MapboxMap) {
        mLatitude = latLng.latitude
        //origin = Point.fromLngLat(latLng.longitude, latLng.latitude)
        LocationHelperAssistant.setZoomMarker(latLng.latitude, latLng.longitude, mapbox, zoom)
        mLongitude = latLng.longitude
        if (mLocationMarker != null) {
            mLocationMarker!!.remove()
        }
        mLocationMarker = mapbox.addMarker(
            MarkerOptions().position(LatLng(latLng.latitude, latLng.longitude))
        )
    }

    private fun setLocationMarkerDestination(latLng: LatLng, mapbox: MapboxMap) {

        //origin = Point.fromLngLat(latLng.longitude, latLng.latitude)
        LocationHelperAssistant.setZoomMarker(latLng.latitude, latLng.longitude, mapbox, zoom)
        if (mLocationMarkerDestination != null) {
            mLocationMarkerDestination!!.remove()
        }
        mLocationMarkerDestination = mapbox.addMarker(
            MarkerOptions().position(LatLng(latLng.latitude, latLng.longitude))
        )
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
                                binding.locationOne.text = feature.text()!!

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
                                mLatitudeTwo =feature.center()?.coordinates()!!.get(1)
                                mLongitudeTwo = feature.center()?.coordinates()!!.get(0)
                                binding.locationTwo.text = feature.text()!!
                                setLocationMarkerTwo(
                                    LatLng(mLatitudeTwo, mLongitudeTwo),
                                    mapbox
                                )

                            }
                        }
                    }
                }
            }
            3 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val feature = PlaceAutocomplete.getPlace(data)
                    if (feature != null) {
                        if (feature.center() != null) {
                            if (feature.center()!!.coordinates().isNotEmpty()) {
                                mLatitudeThree = feature.center()?.coordinates()!!.get(1)
                                mLongitudeThree = feature.center()?.coordinates()!!.get(0)
                                setLocationMarkerThree(LatLng(mLatitudeThree,mLongitudeThree),mapbox)
                                binding.locationThree.text = feature.text()!!

                            }
                        }
                    }
                }
            }
            4 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val feature = PlaceAutocomplete.getPlace(data)
                    if (feature != null) {
                        if (feature.center() != null) {
                            if (feature.center()!!.coordinates().isNotEmpty()) {
                                mLatitudeDestination = feature.center()?.coordinates()!!.get(1)
                                mLongitudeDestination = feature.center()?.coordinates()!!.get(0)
                                binding.locationDestination.text = feature.text()!!
                                setLocationMarkerDestination(
                                    LatLng(mLatitudeDestination, mLongitudeDestination),
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
                    latLongFromAddress(voiceText, requestCode)
                    Log.d("onActivityResult", "onActivityResult  voiceText: $voiceText")
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
            303 -> {
                if (requestCode == 303 && resultCode == Activity.RESULT_OK) {
                    val arrayList: ArrayList<String> =
                        data !!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                    val voiceText: String = arrayList.get(0)
                    latLongFromAddress(voiceText, requestCode)
                    Log.d("onActivityResult", "onActivityResult  voiceText: $voiceText")
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
            304 -> {
                if (requestCode == 304 && resultCode == Activity.RESULT_OK) {
                    val arrayList: ArrayList<String> =
                        data !!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                    val voiceText: String = arrayList.get(0)
                    latLongFromAddress(voiceText, requestCode)
                    Log.d("onActivityResult", "onActivityResult  voiceText: $voiceText")
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setLocationMarkerTwo(latLng: LatLng, mapbox: MapboxMap) {

        LocationHelperAssistant.setZoomMarker(
            latLng.latitude,
            latLng.longitude,
            mapbox,
            zoom
        )
        if (mLocationMarkerTwo != null) {
            mLocationMarkerTwo!!.remove()
        }
        mLocationMarkerTwo = mapbox.addMarker(
            MarkerOptions().position(
                LatLng(
                    latLng.latitude,
                    latLng.longitude
                )
            )
        )
    }

    private fun setLocationMarkerThree(latLng: LatLng, mapbox: MapboxMap) {
        LocationHelperAssistant.setZoomMarker(
            latLng.latitude,
            latLng.longitude,
            mapbox,
            zoom
        )
        if (mLocationMarkerThree != null) {
            mLocationMarkerThree!!.remove()
        }
        mLocationMarkerThree = mapbox.addMarker(
            MarkerOptions().position(
                LatLng(
                    latLng.latitude,
                    latLng.longitude
                )
            )
        )
    }

    private fun latLongFromAddress(voiceText: String, requestCode: Int) {
        if (voiceText !=""){
            StreetViewGeocoderFromAddress(this,voiceText,object :StreetViewGeocoderFromAddress.GeoTaskCallback{
                override fun onSuccessLocationFetched(fetchedLatLngs: LatLng?) {
                    Log.d("onActivityResult", "onActivityResult  voiceText: ${fetchedLatLngs!!.latitude}")
                    when (requestCode){
                        302->{
                            binding.locationTwo.setText(voiceText)
                            setLocationMarkerTwo(fetchedLatLngs,mapbox)
                        }
                        303->{
                            binding.locationThree.setText(voiceText)
                            setLocationMarkerThree(fetchedLatLngs,mapbox)
                        }
                        304->{
                            binding.locationDestination.setText(voiceText)
                            setLocationMarkerDestination(fetchedLatLngs,mapbox)
                        }
                    }
                }

                override fun onFailedLocationFetched() {
                    Log.d("onActivityResult", "onActivityResult  error:")
                }

            }).execute()
        }
    }


    private fun mapStyleOptionClickListener() {

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
    }

    private fun initBannerAd() {
        LoadAdsStreetViewClock.loadEarthLiveMapBannerAdMob(
            binding.bannerAd.adContainer,
            binding.bannerID,
            this
        )
    }

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR,"#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second," #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.bottomSheet.viewTop.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.bottomImage.setColorFilter( Color.parseColor(backgroundSecondColor) )
        binding.locationOnBorderOne.setColorFilter( Color.parseColor(backgroundColor) )
        binding.locationOnBorderTwo.setColorFilter( Color.parseColor(backgroundColor) )
        binding.locationOnBorderThree.setColorFilter( Color.parseColor(backgroundColor) )
        binding.locationOnBorderFour.setColorFilter( Color.parseColor(backgroundColor) )
        binding.img7.setColorFilter( Color.parseColor(backgroundColor) )
        binding.mapStyleOption.setCardBackgroundColor( Color.parseColor(backgroundColor))
        binding.currentLocationImage.setCardBackgroundColor( Color.parseColor(backgroundColor))
        binding.addMoreLocation.setCardBackgroundColor( Color.parseColor(backgroundColor))
        binding.voiceDestinationBtn.setCardBackgroundColor( Color.parseColor(backgroundColor))
        binding.voiceThreeLocation.setCardBackgroundColor( Color.parseColor(backgroundColor))
        binding.voiceTwoLocation.setCardBackgroundColor( Color.parseColor(backgroundColor))
        binding.navigationBtn.setBackgroundColor(Color.parseColor(backgroundColor))
    }

}