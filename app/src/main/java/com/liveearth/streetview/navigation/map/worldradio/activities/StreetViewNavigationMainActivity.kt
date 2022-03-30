package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewNavigationMainBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.navigation.base.internal.route.RouteUrl
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesRequestCallback
import com.mapbox.navigation.ui.NavigationView
import com.mapbox.navigation.ui.NavigationViewOptions
import com.mapbox.navigation.ui.OnNavigationReadyCallback
import com.mapbox.navigation.ui.listeners.NavigationListener
import com.mapbox.navigation.ui.map.NavigationMapboxMap
@SuppressLint("LogNotTimber")
class StreetViewNavigationMainActivity : AppCompatActivity(), NavigationListener,
    OnNavigationReadyCallback {
    private lateinit var binding:ActivityStreetViewNavigationMainBinding
    val TAG = "NavigationMain"

    private lateinit var navigationMapboxMap: NavigationMapboxMap
    private lateinit var mapboxNavigation: MapboxNavigation

    private var origin: Point? = null
    private var destination: Point? = null
    private val INITIAL_ZOOM = 14
    private var navigationView: NavigationView?=null
    private var locationList = ArrayList<Point>()
    private var mCustomRoutList: ArrayList<LatLng> = ArrayList()
    private var mCustomRoutListPoints: ArrayList<Point> = ArrayList()

    var mMultiPointsRoute:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(applicationContext, ConstantsStreetView.accessToken)
        binding = ActivityStreetViewNavigationMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationView=findViewById(R.id.routeNavView)

        mMultiPointsRoute = intent.getBooleanExtra(ConstantsStreetView.MultiPointsRoute,false)
        mCustomRoutList = intent.getParcelableArrayListExtra<LatLng>(ConstantsStreetView.MultiPointsRouteList) as ArrayList<LatLng>
        val lat = intent.getDoubleExtra(ConstantsStreetView.OriginLatitude,0.0)
        val long = intent.getDoubleExtra(ConstantsStreetView.OriginLongitude,0.0)
        val destinationlat = intent.getDoubleExtra(ConstantsStreetView.DestinationLatitude,0.0)
        val destinationLong = intent.getDoubleExtra(ConstantsStreetView.DestinationLongitude,0.0)

        origin = Point.fromLngLat(long,lat)
        destination = Point.fromLngLat(destinationLong,destinationlat)

        if (mMultiPointsRoute){
            mCustomRoutListPoints.add(origin!!)
            for (i in 0 until mCustomRoutList.size){
                mCustomRoutListPoints.add(Point.fromLngLat(mCustomRoutList[i].longitude,mCustomRoutList[i].latitude))
            }
        }

        val initialPosition = CameraPosition.Builder()
            .target(LatLng(origin!!.latitude(),origin!!.longitude())).zoom(INITIAL_ZOOM.toDouble()).build()

        navigationView!!.onCreate(savedInstanceState)
        navigationView!!.initialize(this, initialPosition)
        val feedbackFab = findViewById<View>(R.id.feedbackFab)
        feedbackFab.visibility = View.GONE


    }

    override fun onCancelNavigation() {
        navigationView!!.stopNavigation()
        finish()
    }

    override fun onNavigationFinished() {
        finish()
        Log.d(TAG, "onNavigationFinished: ")
    }

    override fun onNavigationRunning() {
        Log.d(TAG, "onNavigationRunning: ")
    }


    override fun onNavigationReady(isRunning: Boolean) {
        Log.d(TAG, "onNavigationReady: $isRunning")
        if (!isRunning && !::navigationMapboxMap.isInitialized
            && !::mapboxNavigation.isInitialized
        ) {
            if (navigationView!!.retrieveNavigationMapboxMap() != null) {
                Log.d(TAG, "onNavigationReady: Both are not null")
                navigationMapboxMap = navigationView!!.retrieveNavigationMapboxMap()!!
                val navigationOptions = MapboxNavigation
                    .defaultNavigationOptionsBuilder(this, ConstantsStreetView.accessToken)
                    .build()
                if (Build.VERSION.SDK_INT<31){
                    mapboxNavigation = MapboxNavigationProvider.create(navigationOptions)
                    if (mCustomRoutListPoints.size>0){
                        fetchNavigationRouteWithList(mCustomRoutListPoints)
                    }else{
                        fetchNavigationRoute()
                    }

                }
            } else {
                Log.d(TAG, "onNavigationReady:  Both are null")
            }
        }
    }





    private fun fetchNavigationRoute() {
        Log.d(TAG, "fetchRoute: ")
        //   Log.d("searchNavigationRoute", "======coh========: ${LocationHelper.locationPointsList!!.size}")
        if (origin != null && destination != null) {
            mapboxNavigation.requestRoutes(
                RouteOptions.builder()
                    .accessToken(ConstantsStreetView.accessToken)
                    .coordinates(listOf(origin,destination))
                    .geometries(RouteUrl.GEOMETRY_POLYLINE6)
                    .profile(RouteUrl.PROFILE_DRIVING)
                    .user(RouteUrl.PROFILE_DEFAULT_USER)
                    .baseUrl(RouteUrl.BASE_URL)
                    .requestUuid("1")
                    .build(), object : RoutesRequestCallback {
                    override fun onRoutesReady(routes: List<DirectionsRoute>) {
                        Log.d(TAG, "onRoutesReady: ")
                        if (routes.isNotEmpty()) {
                            val directionsRoute = routes[0]
                            startNavigation(directionsRoute)
                        }
                    }

                    override fun onRoutesRequestCanceled(routeOptions: RouteOptions) {
                        Log.d(TAG, "onRoutesRequestCanceled: ")
                    }

                    override fun onRoutesRequestFailure(
                        throwable: Throwable,
                        routeOptions: RouteOptions
                    ) {
                        Log.d(TAG, "onRoutesRequestFailure: ")
                    }
                }
            )
        }
    }

    private fun fetchNavigationRouteWithList(mCustomRoutListPoints: ArrayList<Point>) {
        Log.d(TAG, "fetchRouteList: ")
        //customRoutListPoints
        if (origin != null /*&& destination != null*/) {
            mapboxNavigation.requestRoutes(
                RouteOptions.builder()
                    .accessToken(ConstantsStreetView.accessToken)
                    .coordinates(mCustomRoutListPoints)
                    .geometries(RouteUrl.GEOMETRY_POLYLINE6)
                    .profile(RouteUrl.PROFILE_DRIVING)
                    .user(RouteUrl.PROFILE_DEFAULT_USER)
                    .baseUrl(RouteUrl.BASE_URL)
                    .requestUuid("1")
                    .build(), object : RoutesRequestCallback {
                    override fun onRoutesReady(routes: List<DirectionsRoute>) {
                        Log.d(TAG, "onRoutesReady: ")
                        if (routes.isNotEmpty()) {
                            val directionsRoute = routes[0]
                            startNavigation(directionsRoute)
                        }
                    }

                    override fun onRoutesRequestCanceled(routeOptions: RouteOptions) {
                        Log.d(TAG, "onRoutesRequestCanceled: ")
                    }

                    override fun onRoutesRequestFailure(
                        throwable: Throwable,
                        routeOptions: RouteOptions
                    ) {
                        Log.d(TAG, "onRoutesRequestFailure: ")
                    }
                }
            )
        }
    }



    private fun startNavigation(directionsRoute: DirectionsRoute) {
        Log.d(TAG, "startNavigation: ")
        val optionsBuilder = NavigationViewOptions.builder(this)
        optionsBuilder.navigationListener(this)
        optionsBuilder.directionsRoute(directionsRoute)
        optionsBuilder.shouldSimulateRoute(false)
        navigationView!!.startNavigation(optionsBuilder.build())

    }


    override fun onLowMemory() {
        super.onLowMemory()
        navigationView!!.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        navigationView!!.onStart()
    }

    override fun onResume() {
        super.onResume()
        navigationView!!.onResume()
    }

    override fun onStop() {
        navigationView!!.onStop()
        super.onStop()
    }

    override fun onPause() {
        navigationView!!.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        navigationView!!.onDestroy()
        try {
            if (::mapboxNavigation.isInitialized) {
                mapboxNavigation.onDestroy()
            }
        } catch (e: Exception) {
        }
        super.onDestroy()
    }


}