package com.liveearth.streetview.navigation.map.worldradio.locationTracking

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityTrackedRouteLocationsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationViewModelFactory
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.MapboxDirections
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.core.constants.Constants
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.Marker
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.properties.Delegates

class TrackedRouteLocationsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding:ActivityTrackedRouteLocationsBinding

    private val TAG = "TrackedRouteLocations"
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass
    private var mapbox: MapboxMap? = null
    private var myStyle: Style? = null
    private var locationMarker: Marker? = null
    private var locationMarkerTwo: Marker? = null
    private lateinit var mapView: MapView
    private var origin: Point? = null
    private var destination: Point? = null
    private var currentRoute: DirectionsRoute? = null
    private var client: MapboxDirections? = null
    var zoom: Int = 8
    private var ROUTE_SOURCE_ID: String = "route-source-id"
    private var ROUTE_LAYER_ID: String = "route-layer-id"
    private var ICON_SOURCE_ID: String = "icon-source-id"
    private var customRoutList: ArrayList<LatLng> = ArrayList()
    private var customRoutListPoints: ArrayList<Point> = ArrayList()

    private var mIdData by Delegates.notNull<Int>()
    private lateinit var mTrackLocationViewModel: TrackLocationViewModel
    private lateinit var mTrackedLocationModel:TrackLocationModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityTrackedRouteLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = findViewById(R.id.mapViewNaviRouteTracked)
        mapView.onCreate(savedInstanceState)
        mPreferenceManagerClass = PreferenceManagerClass(this)

        try {
            mIdData = intent.getIntExtra(ConstantsStreetView.dataIdSaved,0)
            getRouteDataFromDatabase(mIdData)
        } catch (e: Exception) {
        }

        clickListener()
        mapViewResultRoute()
    }

    private fun clickListener() {
        binding.toolbarLt.backLink.setOnClickListener {
            onBackPressed()
        }
        binding.toolbarLt.titleTx.text = "Tracked Location Route"
    }

    private fun mapViewResultRoute() {
        mapView.getMapAsync(this)
    }
    @SuppressLint("MissingPermission")
    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapbox = mapboxMap
        mapboxMap.uiSettings.isCompassEnabled = false
        mapboxMap.setStyle(
            Style.MAPBOX_STREETS
        ) { style ->
            myStyle = style

            val locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build())
            locationComponent.isLocationComponentEnabled = true
            locationComponent.renderMode = RenderMode.NORMAL

            initLayers(myStyle!!)
            initSource(myStyle!!)
            setData(mTrackedLocationModel)
            animateStreetViewToBounds()
        }
    }

    private fun getRouteDataFromDatabase(mIdData: Int) {
        try {
            val trackFactory = TrackLocationViewModelFactory(this)
            mTrackLocationViewModel =
                ViewModelProvider(this, trackFactory).get(TrackLocationViewModel::class.java)

            GlobalScope.launch {
                Dispatchers.IO
                mTrackedLocationModel = mTrackLocationViewModel.getDataById(mIdData)
                origin = Point.fromLngLat(mTrackedLocationModel.locationsList!![0].longi, mTrackedLocationModel.locationsList!![0].lati)
                destination = Point.fromLngLat(mTrackedLocationModel.locationsList!![mTrackedLocationModel.locationsList!!.size-1].longi,
                    mTrackedLocationModel.locationsList!![mTrackedLocationModel.locationsList!!.size-1].lati)
                //setData(mTrackLocationViewModel.getDataById(mIdData))
            }


        } catch (e: Exception) {
        }
    }

    private fun setData(dataById: TrackLocationModel) {
        for (i in 0 until dataById.locationsList!!.size){
            customRoutListPoints.add(Point.fromLngLat(dataById.locationsList!![i].longi,dataById.locationsList!![i].lati))
        }
        getRouteList(mapbox!!,customRoutListPoints,origin!!)

        for (i in 0 until customRoutListPoints.size){
            mapbox!!.addMarker(
                MarkerOptions().position(
                    LatLng(
                        customRoutListPoints[i].latitude(),
                        customRoutListPoints[i].longitude()
                    )
                )
            )
        }
    }


    private fun getRouteList(mapboxMap: MapboxMap, wayPointsList: ArrayList<Point>,origin:Point) {
        val distanceProfile = DirectionsCriteria.PROFILE_DRIVING
        client = MapboxDirections.builder()
            .origin(origin)
            .waypoints(wayPointsList)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(distanceProfile)
            .accessToken(ConstantsStreetView.accessToken)
            .build()
//            .origin(origin)
//            .destination(destination)

        client!!.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {
                Toast.makeText(
                    this@TrackedRouteLocationsActivity,
                    "Cannot Find Route!\nTry again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<DirectionsResponse?>,
                response: Response<DirectionsResponse?>
            ) {
                if (response.body() == null) {
                    Log.d(TAG, "onResponse: Cannot Find Route!No route found  ")
                    return
                } else if (response.body()!!.routes().size < 1) {
                    Log.d(TAG, "onResponse: Cannot Find Route!No route found  ")

                    Toast.makeText(
                        this@TrackedRouteLocationsActivity,
                        "Cannot Find Route!No route found ",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                currentRoute = response.body()!!.routes()[0]
                val durationDistance = currentRoute!!.duration().toInt()
                //ConvertSectoDay(durationDistance)
                var distance = (currentRoute!!.distance().toDouble() / 1000)
       /*         binding.distanceRouteTv.text = distance.toString()
                try {
                    if (mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Miles,false)){
                        distance= distance*0.6214
                        binding.distanceRouteTv.text = DecimalFormat("#.#").format(distance)
                        binding.milesTv.text = "Miles"
                    }else{
                        binding.distanceRouteTv.text = DecimalFormat("#.#").format(distance)
                        binding.milesTv.text = "Km"
                    }
                } catch (e: Exception) {
                }*/
                mapboxMap.getStyle { style ->
                    val source =
                        style.getSourceAs<GeoJsonSource>(ROUTE_SOURCE_ID)
                    if (source != null) {
                        Log.d(TAG, "onResponse: $origin ==   ")
                        Timber.d("onResponse: source != null")
                        source.setGeoJson(
                            FeatureCollection.fromFeature(
                                Feature.fromGeometry(
                                    LineString.fromPolyline(
                                        currentRoute!!.geometry()!!,
                                        Constants.PRECISION_6
                                    )
                                )
                            )
                        )
                    } else {
                        Toast.makeText(
                            this@TrackedRouteLocationsActivity,
                            "Cannot find Route!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        })
    }


    private fun animateStreetViewToBounds() {
        val latLngBounds = LatLngBounds.Builder()
            .include(LatLng(origin!!.latitude(), origin!!.longitude()))
            .include(LatLng(destination!!.latitude(), destination!!.longitude()))
            .build()
        try {
            mapbox!!.animateCamera(
                CameraUpdateFactory.newLatLngBounds(latLngBounds, 150),
                5000
            )
        } catch (e: Exception) {
        }
    }

    private fun initSource(@NonNull loadedMapStyle: Style) {
        loadedMapStyle.addSource(GeoJsonSource(ROUTE_SOURCE_ID))
        val iconGeoJsonSource = GeoJsonSource(
            ICON_SOURCE_ID, FeatureCollection.fromFeatures(
                arrayOf(
                    Feature.fromGeometry(
                        Point.fromLngLat(
                            origin!!.longitude(),
                            origin!!.latitude()
                        )
                    ),
                    Feature.fromGeometry(
                        Point.fromLngLat(
                            destination!!.longitude(),
                            destination!!.latitude()
                        )
                    )
                )
            )
        )
        loadedMapStyle.addSource(iconGeoJsonSource)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initLayers(loadedMapStyle: Style) {
        val routeLayer = LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID)
        // Add the LineLayer to the map. This layer will display the directions route.
        routeLayer.setProperties(
            PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
            PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
            PropertyFactory.lineWidth(5f),
            PropertyFactory.lineColor(Color.RED)
        )
        loadedMapStyle.addLayer(routeLayer)
    }

}