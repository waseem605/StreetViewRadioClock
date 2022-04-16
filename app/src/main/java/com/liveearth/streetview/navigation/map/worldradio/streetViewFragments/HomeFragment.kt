package com.liveearth.streetview.navigation.map.worldradio.streetViewFragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.centurionnavigation.callBack.LiveEarthAddressFromLatLng
import com.example.centurionnavigation.dialogs.LocationRequestDialogueBox
import com.example.dummy.apiServices.WeatherAPI
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper
import com.example.dummy.apiServices.WeatherAPIServices
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewWeatherCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm.RepositoryWeather
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm.RetrofitHelper
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm.ViewModelFactory
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm.WeatherViewModel
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExistCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.HomeFragmentClickCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.WeatherList
import com.liveearth.streetview.navigation.map.worldradio.activities.*
import com.liveearth.streetview.navigation.map.worldradio.databinding.FragmentHomeBinding
import com.liveearth.streetview.navigation.map.worldradio.locationTracking.LocationTrackingMainActivity
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.HomeFragmentMoreAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.HomeFragmentTopAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.HomeFragmentModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val TAG = "HomeFragment"
    private lateinit var mHomeTopAdapter: HomeFragmentTopAdapter
    private lateinit var mHomeMoreAdapter: HomeFragmentMoreAdapter
    private var mTopList: ArrayList<HomeFragmentModel> = ArrayList()
    private var mBottomList: ArrayList<HomeFragmentModel> = ArrayList()
    var mLocationRepository: LocationRepository? = null
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass
    private lateinit var viewModel: WeatherViewModel
    private var lat:Double = 0.0
    private var lon:Double = 0.0
    private var update = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        checkLocationPermission()
        mPreferenceManagerClass = PreferenceManagerClass(requireContext())
        topItemManager()
        moreItemManager()
        currentLocationWeather()
        binding.forwardWeather.setOnClickListener {
            weatherIntentLatLng()
        }

        return binding.root
    }

    private fun topItemManager() {

        mTopList.add(HomeFragmentModel(R.drawable.ic_live_earth_icon, "Live Earth Map", 0))
        mTopList.add(HomeFragmentModel(R.drawable.ic_street_map_icon, "Live Street View", 1))
        mTopList.add(HomeFragmentModel(R.drawable.ic_navigation_compass_icon, "Navigation", 2))
        mHomeTopAdapter = HomeFragmentTopAdapter(mTopList, requireContext(), object :
            HomeFragmentClickCallBack {
            override fun onItemClickListener(mode: HomeFragmentModel) {

                topItemClickListeners(mode.pos)
            }

        })
        binding.topRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = mHomeTopAdapter
        }
    }

    private fun topItemClickListeners(pos: Int) {
        when(pos){
            0->{
                val mainIntent = Intent(requireContext(), StreetViewLiveEarthActivity::class.java)
                startActivity(mainIntent)
            }
            1->{
                //val mainIntent = Intent(requireContext(),StreetViewLiveEarthActivity::class.java)
                val mainIntent = Intent(requireContext(),StreetViewFirstLookActivity::class.java)
                startActivity(mainIntent)
            }
            2->{
                val mainIntent = Intent(requireContext(),StreetViewSearchNavigationActivity::class.java)
                startActivity(mainIntent)
            }
        }
    }

    private fun moreItemManager() {

        mBottomList.add(HomeFragmentModel(R.drawable.icon_meet_me, "Meet Me", 0))
        mBottomList.add(HomeFragmentModel(R.drawable.cloudy_icon, "My favourite", 1))
        mBottomList.add(HomeFragmentModel(R.drawable.location_tracker_icon, "Location Tracker", 2))
        mBottomList.add(HomeFragmentModel(R.drawable.icon_clock, "World Clock", 3))
        mBottomList.add(HomeFragmentModel(R.drawable.icon_radio, "Radio", 4))
        mBottomList.add(HomeFragmentModel(R.drawable.speedo_meter, "Speedometer", 5))
        mBottomList.add(HomeFragmentModel(R.drawable.car_icon, "Travel Expense", 6))
        mBottomList.add(HomeFragmentModel(R.drawable.cloudy_icon, "Weather", 7))

        mHomeMoreAdapter = HomeFragmentMoreAdapter(mBottomList, requireContext(), object :
            HomeFragmentClickCallBack {
            override fun onItemClickListener(mode: HomeFragmentModel) {
                moreItemClickListener(mode.pos)
            }

        })
        binding.moreRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(),4)
            adapter = mHomeMoreAdapter
        }
    }

    private fun moreItemClickListener(pos: Int) {
        when(pos){
            0->{
                val mainIntent = Intent(requireContext(),StreetViewMeetMeActivity::class.java)
                startActivity(mainIntent)
            }
            1->{
                val mainIntent = Intent(requireContext(),StreetViewFavouriteLocationsActivity::class.java)
                startActivity(mainIntent)
            }
            2->{
                val mainIntent = Intent(requireContext(), LocationTrackingMainActivity::class.java)
                startActivity(mainIntent)
            }
            3->{
                val mainIntent = Intent(requireContext(),StreetViewWorldClockActivity::class.java)
                startActivity(mainIntent)
            }
            4->{
                val mainIntent = Intent(requireContext(),StreetViewMainGlobeViewActivity::class.java)
                startActivity(mainIntent)
            }
            5->{
                val mainIntent = Intent(requireContext(),StreetViewSpeedoMeterActivity::class.java)
                startActivity(mainIntent)
            }
            6->{
                val mainIntent = Intent(requireContext(),StreetViewTravelExpenseViewActivity::class.java)
                startActivity(mainIntent)
            }
            7->{
                weatherIntentLatLng()
            }

        }


    }

    private fun weatherIntentLatLng() {
        Log.d(TAG, "weatherIntentLatLng: ===========button pressed")
        val mainIntent = Intent(requireContext(),StreetViewWeatherDetailsActivity::class.java)
        mainIntent.putExtra(ConstantsStreetView.OriginLatitude,lat)
        mainIntent.putExtra(ConstantsStreetView.OriginLongitude,lon)
        startActivity(mainIntent)
    }



    private fun currentLocationWeather() {
        mLocationRepository = LocationRepository(requireContext(), object : MyLocationListener {
            override fun onLocationChanged(location: Location) {
                location.let {
                    mLocationRepository!!.stopLocation()
                    lat = it.latitude
                    lon = it.longitude
                    //homeWeatherDetails(it)
                    homeWeatherMVVMDetails(it)

                    LiveEarthAddressFromLatLng(requireContext(), LatLng(it.latitude,it.longitude),object :
                        LiveEarthAddressFromLatLng.GeoTaskCallback{
                        override fun onSuccessLocationFetched(fetchedAddress: String?) {
                            binding.currentAddress.text = fetchedAddress
                            ConstantsStreetView.CURRENT_ADDRESS = fetchedAddress!!

                        }

                        override fun onFailedLocationFetched() {
                        }
                    }).execute()

                }
            }

        })
    }

    private fun homeWeatherMVVMDetails(location: Location) {
        val services = RetrofitHelper.getInstance().create(WeatherAPI::class.java)
        val repositoryWeather = RepositoryWeather(services)
        viewModel = ViewModelProvider(this, ViewModelFactory(repositoryWeather,location.latitude.toString(),location.longitude.toString())).get(
            WeatherViewModel::class.java)

        viewModel.weather.observe(this, Observer {

            try {
                StreetViewWeatherHelper.arrayListWeather = it.list as ArrayList<WeatherList>

                val temperatureUnit = mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)
                if (temperatureUnit){
                    binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToForenHeat(it.list[0].main.temp).toString()
                    binding.weatherUnit.text = "F"
                }else{
                    binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToCelsius(it.list[0].main.temp).toString()
                    binding.weatherUnit.text = "C"
                }
                //binding.weatherTemp.text = ""+StreetViewWeatherHelper.kalvinToCelsius(it.list[0].main.temp).toString()
                Log.d("454545454","==========="+ StreetViewWeatherHelper.kalvinToCelsius(it.list[0].main.temp).toString())
                try {
                    Glide.with(requireContext())
                        .load(StreetViewWeatherHelper.getIcon(it.list[0].weather[0].icon))
                        .into(binding.weatherTodayIcon)
                }catch (e: Exception) {
                    println(e)
                }
                binding.todayDate.text = StreetViewWeatherHelper.getWeatherDate(it.list[0].dt.toLong(), 1)
                binding.weatherTodayType.text = it.list[0].weather[0].main.toString()

            } catch (e: Exception) {
            }
        })
    }

    private fun homeWeatherDetails(location: Location) {
        val weatherToday = WeatherAPIServices(object :StreetViewWeatherCallBack{
            override fun onSuccess(data: StreetViewWeatherModel) {
                StreetViewWeatherHelper.arrayListWeather=data.list as ArrayList<WeatherList>
                setData(data)
                try {
                    Glide.with(this@HomeFragment).load(StreetViewWeatherHelper.getIcon(data.list[0].weather[0].icon)).into(binding.weatherTodayIcon)
                    binding.todayDate.text =  StreetViewWeatherHelper.getWeatherDate(data.list[0].dt.toLong(), 1)
                    binding.weatherTodayType.text = data.list[0].weather[0].main.toString()
                } catch (e: Exception) {
                }
            }

            override fun onFailure(userError: String) {
                Log.d("weatherDetails", "onSuccess: ===$userError====onFailure=")
            }

            override fun onLoading(loading: Boolean) {
                Log.d("weatherDetails", "onSuccess: =======onLoading=")
            }
        })

        if (update<2) {
            GlobalScope.launch {
                Dispatchers.IO
                try {
                    if (location !=null) {
                        weatherToday.getDataService(
                            location.latitude.toString(), location.longitude.toString()
                        )
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "weatherDetails: $e")
                }
            }
            update++
        }
    }

    private fun setData(data: StreetViewWeatherModel) {
        //binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToCelsius(data.list[0].main.temp).toString()
        val temperatureUnit = mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)
        if (temperatureUnit){
            binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToForenHeat(data.list[0].main.temp).toString()
            binding.weatherUnit.text = "F"
        }else{
            binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToCelsius(data.list[0].main.temp).toString()
            binding.weatherUnit.text = "C"
        }
    }


    fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                val permissionDialog = LocationRequestDialogueBox(requireContext(),object :
                    ExistCallBackListener {
                    override fun onExistClick() {
                        requestLocationPermission()
                    }
                })
                permissionDialog.show()
            } else {

                requestLocationPermission()
            }

        }else{
            currentLocationWeather()
            //mainItemClickListener()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                currentLocationWeather()
                //mainItemClickListener()
            }
        } else {
            Toast.makeText(requireContext(), "permission denied", Toast.LENGTH_LONG).show()
            if (! ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", requireContext().packageName, null),
                    ),
                )
            }
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }



}