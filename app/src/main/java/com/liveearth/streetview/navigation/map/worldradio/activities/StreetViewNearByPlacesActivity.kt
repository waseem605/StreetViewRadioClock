package com.liveearth.streetview.navigation.map.worldradio.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
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
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationViewModelFactory
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StreetViewNearByPlacesActivity : BaseStreetViewActivity(), OnMapReadyCallback {
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
    private var nearLocationMarkerZoom: Marker? = null
    private var mOrigin: Point? = null
    private var mDestination: Point? = null
    var zoom: Int = 12
    var flagMap: Boolean = true
    lateinit var animationDownToUp: Animation
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass
    private var mLocationsList: ArrayList<NearLocationsModel> = ArrayList()
    private lateinit var myRepository: LocationRepository
    private lateinit var adapterLocations:NearMeLocationsAdapter
    private lateinit var mFavouriteLocationViewModel: FavouriteLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityStreetViewNearByPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
        val factory = FavouriteLocationViewModelFactory(this)
        mFavouriteLocationViewModel = ViewModelProvider(this,factory).get(FavouriteLocationViewModel::class.java)

        mapView = findViewById(R.id.mapViewNearMe)
        mapView.onCreate(savedInstanceState)
        mapViewResult()
        animationDownToUp = AnimationUtils.loadAnimation(this, R.anim.slide_point_up)


        binding.bottomLayout.setOnClickListener {  }
        binding.toolbarLt.titleTx.text = "Near By"
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
        mapboxMap.uiSettings.logoGravity = Gravity.TOP
        mapboxMap.uiSettings.attributionGravity = Gravity.TOP

        getYourCurrentLocation()

    /*    binding.meetMeSearchBtn.setOnClickListener {
            searchNearLocation()
        }*/

    }

    private fun searchNearLocation(latitude: Double, longitude: Double) {
        showProgressDialog(this)
        val apiLocationServices = StreetViewLocationAPIServices(object : StreetViewNearByCallBack {
            override fun onSuccess(data: StreetViewNearPlacesModel) {
                val nearLocationData = data.results
                hideProgressDialog(this@StreetViewNearByPlacesActivity)
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
         binding.bottomLayout.startAnimation(animationDownToUp)
         binding.bottomLayout.visibility = View.VISIBLE
         binding.meetMeSearchBtn.visibility = View.GONE
         binding.helpingLayout.visibility = View.VISIBLE

        try {
            adapterLocations = NearMeLocationsAdapter(nearLocationData, this,mFavouriteLocationViewModel, object : StreetViewNearMeCallBack {
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
                    LocationHelper.shareLocation(this@StreetViewNearByPlacesActivity,model.geocodes.main.latitude,model.geocodes.main.longitude)
                }

                override fun addToFavouriteLocation(model: Result) {
                    setToast(this@StreetViewNearByPlacesActivity,"Added to favourite")
                    val fvrModel = FavouriteLocationModel(id = null,model.fsq_id,model.location.address,model.name,model.timezone,LocationHelper.getCurrentDateTime(this@StreetViewNearByPlacesActivity,2),
                        LocationHelper.getCurrentDateTime(this@StreetViewNearByPlacesActivity,3),model.geocodes.main.latitude,model.geocodes.main.longitude)
                    mFavouriteLocationViewModel.insertFavouriteLocation(fvrModel)
                    adapterLocations.notifyDataSetChanged()

                    savedLocationAsFavourite(model)
                }

                override fun removeFromFavouriteLocation(model: Result) {
                    val fvrModel = FavouriteLocationModel(id = null,model.fsq_id,model.location.address,model.name,model.timezone,LocationHelper.getCurrentDateTime(this@StreetViewNearByPlacesActivity,2),
                        LocationHelper.getCurrentDateTime(this@StreetViewNearByPlacesActivity,3),model.geocodes.main.latitude,model.geocodes.main.longitude)

             /*       GlobalScope.launch(Dispatchers.Main) {
                       mFavouriteLocationViewModel.deleteFavouriteLocation(fvrModel)
                        setToast(this@StreetViewNearByPlacesActivity,"Removed from Favourite")
                    }*/

                    GlobalScope.launch(Dispatchers.Main) {
                        mFavouriteLocationViewModel.deleteFavouriteById(model.fsq_id)
                        adapterLocations.notifyDataSetChanged()
                        setToast(this@StreetViewNearByPlacesActivity,"Delete item successfully")
                    }

                }

                override fun onClickOfItemLocation(model: Result, pos: Int) {

                    LocationHelper.setZoomMarker(model.geocodes.main.latitude, model.geocodes.main.longitude, mapbox, 18)
                    if (nearLocationMarkerZoom!=null){
                        nearLocationMarkerZoom!!.remove()
                    }
                    nearLocationMarkerZoom = mapbox.addMarker(
                        MarkerOptions().position(LatLng(model.geocodes.main.latitude, model.geocodes.main.longitude)).title(model.name).icon( IconFactory.getInstance(this@StreetViewNearByPlacesActivity)
                            .fromResource(R.drawable.circle_marker))
                    )
                }

            })
        } catch (e: Exception) {
        }

        binding.locationRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                this@StreetViewNearByPlacesActivity,
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = adapterLocations
        }

    }

    private fun savedLocationAsFavourite(model: Result) {


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
                        searchNearLocation(it.latitude,it.longitude)
                        mLocationMarker = mapbox.addMarker(
                            MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        )
/*                        val latLng = LatLng(it.latitude, it.longitude)
                        LiveEarthAddressFromLatLng(this@StreetViewNearByPlacesActivity,
                            latLng,
                            object : LiveEarthAddressFromLatLng.GeoTaskCallback {
                                override fun onSuccessLocationFetched(fetchedAddress: String?) {
                                    binding.searchCurrentLocation.setText(fetchedAddress)
                                }

                                override fun onFailedLocationFetched() {
                                }
                            }).execute()*/
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


/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val feature = PlaceAutocomplete.getPlace(data)
                    if (feature != null) {
                        if (feature.center() != null) {
                            if (feature.center()!!.coordinates().isNotEmpty()) {
                                binding.searchCurrentLocation.text = feature.text()!!
                                setLocationMarker(
                                    LatLng(feature.center()?.coordinates()!!.get(1),  feature.center()?.coordinates()!!.get(0)),
                                    mapbox
                                )
                            }
                        }
                    }
                }
            }
        }
    }*/

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
        //binding.backTwo.setColorFilter(Color.parseColor(backgroundColor))
    }

}