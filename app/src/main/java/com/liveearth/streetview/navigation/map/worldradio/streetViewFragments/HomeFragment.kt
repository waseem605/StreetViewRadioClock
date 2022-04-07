package com.liveearth.streetview.navigation.map.worldradio.streetViewFragments

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.centurionnavigation.callBack.LiveEarthAddressFromLatLng
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper
import com.example.dummy.apiServices.WeatherAPIServices
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewWeatherCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.HomeFragmentClickCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel
import com.liveearth.streetview.navigation.map.worldradio.activities.*
import com.liveearth.streetview.navigation.map.worldradio.databinding.FragmentHomeBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.HomeFragmentMoreAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.HomeFragmentTopAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.HomeFragmentModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
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
    private var lat:Double = 0.0
    private var lon:Double = 0.0
    private var update = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        topItemManager()
        moreItemManager()
        currentLocationWeather()

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
                val mainIntent = Intent(requireContext(),StreetViewLiveEarthActivity::class.java)
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
        mBottomList.add(HomeFragmentModel(R.drawable.icon_nearby, "Nearby Places", 2))
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
                val mainIntent = Intent(requireContext(),StreetViewNearByPlacesActivity::class.java)
                startActivity(mainIntent)
            }
            3->{
                val mainIntent = Intent(requireContext(),StreetViewWorldClockActivity::class.java)
                startActivity(mainIntent)
            }
            4->{
                val mainIntent = Intent(requireContext(),PathsPolygonsLabelsLiveEarthMapFmActivity::class.java)
                startActivity(mainIntent)
            }
            5->{
                val mainIntent = Intent(requireContext(),StreetViewSpeedoMeterActivity::class.java)
                startActivity(mainIntent)
            }
            6->{
                val mainIntent = Intent(requireContext(),StreetViewTravelExpenseActivity::class.java)
                startActivity(mainIntent)
            }
            7->{
                val mainIntent = Intent(requireContext(),StreetViewWeatherDetailsActivity::class.java)
                mainIntent.putExtra(ConstantsStreetView.OriginLatitude,lat)
                mainIntent.putExtra(ConstantsStreetView.OriginLongitude,lon)
                startActivity(mainIntent)
            }
        }
    }



    private fun currentLocationWeather() {
        mLocationRepository = LocationRepository(requireContext(), object : MyLocationListener {
            override fun onLocationChanged(location: Location) {
                location.let {
                    mLocationRepository!!.stopLocation()
                    lat = it.latitude
                    lon = it.longitude
                    homeWeatherDetails(it)
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

    private fun homeWeatherDetails(location: Location) {
        val weatherToday = WeatherAPIServices(object :StreetViewWeatherCallBack{
            override fun onSuccess(data: StreetViewWeatherModel) {
               // weatherData=data
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
        binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToCelsius(data.list[0].main.temp).toString()
/*        val temperatureUnit = PreferenceManagerClass!!.getBoolean(AppConstants.CELCIUS_TEMP)
        if (temperatureUnit){
            binding.temperatureToday.text = WeatherHelper.kalvinToCelsius(data.list[0].main.temp).toString()
            binding.degreeSignC.text = "C"
        }else{
            binding.temperatureToday.text = WeatherHelper.kalvinToForenHeat(data.list[0].main.temp).toString()
            binding.degreeSignC.text = "F"
        }*/
    }


}