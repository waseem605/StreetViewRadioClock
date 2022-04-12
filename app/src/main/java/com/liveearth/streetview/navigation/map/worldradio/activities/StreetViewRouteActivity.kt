package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewRouteBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

@SuppressLint("LogNotTimber")
class StreetViewRouteActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityStreetViewRouteBinding
    private val TAG = "RouteActivity"

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
    var lat :Double = 0.0
    var long :Double = 0.0
    var destinationlat:Double = 0.0
    var destinationLong:Double = 0.0
    var mMultiPointsRoute:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityStreetViewRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapView = findViewById(R.id.mapViewNaviRoute)
        mapView.onCreate(savedInstanceState)


        try {
            mMultiPointsRoute = intent.getBooleanExtra(ConstantsStreetView.MultiPointsRoute,false)

            lat = intent.getDoubleExtra(ConstantsStreetView.OriginLatitude, 0.0)
            long = intent.getDoubleExtra(ConstantsStreetView.OriginLongitude, 0.0)
            destinationlat = intent.getDoubleExtra(ConstantsStreetView.DestinationLatitude, 0.0)
            destinationLong = intent.getDoubleExtra(ConstantsStreetView.DestinationLongitude, 0.0)
            if (mMultiPointsRoute){
                Log.d(TAG,"==customRoutListPoints===84===")
                //mMultiPointsRoute = true
                customRoutList =
                    intent.getParcelableArrayListExtra<LatLng>(ConstantsStreetView.MultiPointsRouteList) as ArrayList<LatLng>
                for (i in 0 until customRoutList.size){
                    customRoutListPoints.add(Point.fromLngLat(customRoutList[i].longitude,customRoutList[i].latitude))
                }
            }
            origin = Point.fromLngLat(long, lat)
            destination = Point.fromLngLat(destinationLong, destinationlat)
            Log.d(TAG,"==origin=========$origin========$destination=====}")

        } catch (e: Exception) {
            Log.d(TAG,e.message.toString())
        }

        if (Build.VERSION.SDK_INT >= 31) {
            binding.startNavBtn.visibility = View.GONE
        } else {
            binding.startNavBtn.setOnClickListener {
                val intentRoute = Intent(this, StreetViewNavigationMainActivity::class.java)
                intentRoute.putParcelableArrayListExtra(ConstantsStreetView.MultiPointsRouteList, customRoutList)
                intentRoute.putExtra(ConstantsStreetView.OriginLatitude, lat)
                intentRoute.putExtra(ConstantsStreetView.OriginLongitude, long)
                intentRoute.putExtra(ConstantsStreetView.DestinationLatitude, destinationlat)
                intentRoute.putExtra(ConstantsStreetView.DestinationLongitude, destinationLong)
                intentRoute.putExtra(ConstantsStreetView.MultiPointsRoute,mMultiPointsRoute)
                startActivity(intentRoute)
            }
        }
        mapViewResultRoute()


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
            initLayers(myStyle!!)
            initSource(myStyle!!)

            val locationComponent = mapboxMap.locationComponent
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build())
            locationComponent.isLocationComponentEnabled = true
            locationComponent.renderMode = RenderMode.NORMAL

            if(mMultiPointsRoute){
                getRouteList(mapboxMap,customRoutListPoints,origin!!)
                //mapboxMap.addMarker(MarkerOptions().position(LatLng(origin!!.latitude(), origin!!.longitude())))

                for (i in 0 until customRoutListPoints.size){
                    mapboxMap.addMarker(
                        MarkerOptions().position(
                            LatLng(
                                customRoutListPoints[i].latitude(),
                                customRoutListPoints[i].longitude()
                            )
                        )
                    )
                }
            }else{
                mapboxMap.addMarker(MarkerOptions().position(LatLng(origin!!.latitude(), origin!!.longitude())))
                mapboxMap.addMarker(MarkerOptions().position(LatLng(destination!!.latitude(), destination!!.longitude())))
                getRoute(mapboxMap, origin!!, destination!!)
            }
            animateStreetViewToBounds()
        }

        mapboxMap.uiSettings.logoGravity = Gravity.TOP
        mapboxMap.uiSettings.attributionGravity = Gravity.TOP
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

    private fun getRoute(mapboxMap: MapboxMap, origin: Point, destination: Point) {
        val distanceProfile = DirectionsCriteria.PROFILE_DRIVING
        client = MapboxDirections.builder()
            .origin(origin)
            .destination(destination)
            .overview(DirectionsCriteria.OVERVIEW_FULL)
            .profile(distanceProfile)
            .accessToken(ConstantsStreetView.accessToken)
            .build()

        Log.d(
            TAG,
            "====Route==met======" + origin.longitude() + "==" + origin.longitude() + "==" + destination.latitude() + "==" + destination.longitude()
        )

        client!!.enqueueCall(object : Callback<DirectionsResponse?> {
            override fun onFailure(call: Call<DirectionsResponse?>, t: Throwable) {
                Toast.makeText(
                    this@StreetViewRouteActivity,
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
                        this@StreetViewRouteActivity,
                        "Cannot Find Route!No route found ",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                currentRoute = response.body()!!.routes()[0]
                val durationDistance = currentRoute!!.duration().toInt()
                ConvertSectoDay(durationDistance)
                var distance = (currentRoute!!.distance().toDouble() / 1000)
                binding.distanceRouteTv.text = distance.toString()

//                val PreferenceManagerClass = PreferenceManagerClass(this@StreetViewRouteActivity)
//                try {
//                    if (PreferenceManagerClass.getBoolean(AppConstants.DISTANCE_UNIT)){
//                        binding.distanceRouteTv.text = DecimalFormat("#.#").format(distance)
//                        binding.milesTv.text = "Km"
//                    }else{
//                        distance= distance*0.6214
//                        binding.distanceRouteTv.text = DecimalFormat("#.#").format(distance)
//                        binding.milesTv.text = "Miles"
//                    }
//                } catch (e: Exception) {
//                }

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
                            this@StreetViewRouteActivity,
                            "Cannot find Route!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        })
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
                    this@StreetViewRouteActivity,
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
                        this@StreetViewRouteActivity,
                        "Cannot Find Route!No route found ",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                currentRoute = response.body()!!.routes()[0]
                val durationDistance = currentRoute!!.duration().toInt()
                ConvertSectoDay(durationDistance)
                var distance = (currentRoute!!.distance().toDouble() / 1000)
                binding.distanceRouteTv.text = distance.toString()

//                val PreferenceManagerClass = PreferenceManagerClass(this@StreetViewRouteActivity)
//                try {
//                    if (PreferenceManagerClass.getBoolean(AppConstants.DISTANCE_UNIT)){
//                        binding.distanceRouteTv.text = DecimalFormat("#.#").format(distance)
//                        binding.milesTv.text = "Km"
//                    }else{
//                        distance= distance*0.6214
//                        binding.distanceRouteTv.text = DecimalFormat("#.#").format(distance)
//                        binding.milesTv.text = "Miles"
//                    }
//                } catch (e: Exception) {
//                }

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
                            this@StreetViewRouteActivity,
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

    fun ConvertSectoDay(n: Int) {
        var n = n
        val day = n / (24 * 3600)
        n = n % (24 * 3600)
        val hour = n / 3600
        n %= 3600
        val minutes = n / 60
        n %= 60
        val seconds = n
        if (day == 0) {
            binding.timeTv.text = "$hour H $minutes m"
            if (hour == 0) {
                binding.timeTv.text = "$minutes m"
            }
        } else {
            binding.timeTv.text = "$day D $hour H $minutes m"
        }

    }


}