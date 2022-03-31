package com.liveearth.streetview.navigation.map.worldradio.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper
import com.example.dummy.apiServices.WeatherAPIServices
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewWeatherCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WeatherCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewWeatherDetailsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.WeatherDaysAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.HomeFragmentModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StreetViewWeatherDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewWeatherDetailsBinding
    private val TAG = "WeatherDetails"
    private var mLatitude:Double = 0.0
    private var mLongitude:Double = 0.0
    private lateinit var mAdapter:WeatherDaysAdapter

    private var mDaysWeatherList:ArrayList<HomeFragmentModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityStreetViewWeatherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mLatitude = intent.getDoubleExtra(ConstantsStreetView.OriginLatitude,0.0)
        mLongitude = intent.getDoubleExtra(ConstantsStreetView.OriginLongitude,0.0)


        mainWeatherDetails(mLatitude,mLongitude)

        nexDaysDetails()


    }

    private fun nexDaysDetails() {

        mDaysWeatherList.add(HomeFragmentModel(R.drawable.ic_weather_temp,"Sunday",0))
        mDaysWeatherList.add(HomeFragmentModel(R.drawable.ic_weather_temp,"Sunday",0))
        mDaysWeatherList.add(HomeFragmentModel(R.drawable.ic_weather_temp,"Sunday",0))
        mDaysWeatherList.add(HomeFragmentModel(R.drawable.ic_weather_temp,"Sunday",0))
        mDaysWeatherList.add(HomeFragmentModel(R.drawable.ic_weather_temp,"Sunday",0))
        mDaysWeatherList.add(HomeFragmentModel(R.drawable.ic_weather_temp,"Sunday",0))
        mDaysWeatherList.add(HomeFragmentModel(R.drawable.ic_weather_temp,"Sunday",0))
        mDaysWeatherList.add(HomeFragmentModel(R.drawable.ic_weather_temp,"Sunday",0))

        mAdapter = WeatherDaysAdapter(mDaysWeatherList,this,object :WeatherCallBack{
            override fun onItemWeather(model: StreetViewWeatherModel) {

            }

        })

        binding.dayRecycler.apply {
            layoutManager = LinearLayoutManager(this@StreetViewWeatherDetailsActivity)
            adapter = mAdapter
        }
    }


    private fun mainWeatherDetails(mLatitude: Double, mLongitude: Double) {

        Log.d("454545454","=======$mLatitude==$mLongitude==================")

        val weatherResult = WeatherAPIServices(object :StreetViewWeatherCallBack{
            override fun onSuccess(data: StreetViewWeatherModel) {

                binding.weatherToday.text = ""+StreetViewWeatherHelper.kalvinToCelsius(data.list[0].main.temp).toString()
                Log.d("454545454","===============weather============"+ StreetViewWeatherHelper.kalvinToCelsius(data.list[0].main.temp).toString())

/*
                try {
                    Glide.with(this@StreetViewWeatherDetailsActivity)
                        .load(WeatherHelper.getIcon(data.list[0].weather[0].icon))
                        .into(binding.weatherTodayImage)
                }catch (e: Exception) {
                    println(e)
                }*/
                binding.weatherTimeItem.text = StreetViewWeatherHelper.setDate(data.list[0].dt_txt, 1)
                binding.weatherTodayType.text = data.list[0].weather[0].main.toString()
                //forecastWeather(data.list as ArrayList<WeatherList>)

            }

            override fun onFailure(userError: String) {
                Log.d("454545454","==64======error=========$userError==========")

            }

            override fun onLoading(loading: Boolean) {

            }

        })

        GlobalScope.launch {
            Dispatchers.IO
            weatherResult.getDataService(mLatitude.toString(),mLongitude.toString())
        }

    }



}