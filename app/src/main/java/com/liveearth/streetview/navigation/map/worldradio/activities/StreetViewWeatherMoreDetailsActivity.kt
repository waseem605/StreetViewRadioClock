package com.liveearth.streetview.navigation.map.worldradio.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WeatherMoreDetailsCallback
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.WeatherList
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewWeatherMoreDetailsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.WeatherDetailsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper
import java.text.DecimalFormat

class StreetViewWeatherMoreDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewWeatherMoreDetailsBinding
    private val TAG = "WeatherMoreDetails"
    var mArrayListWeather = ArrayList<WeatherList>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewWeatherMoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mArrayListWeather = StreetViewWeatherHelper.arrayListWeather


        forecastWeather(mArrayListWeather)

    }

    private fun forecastWeather(list: ArrayList<WeatherList>) {
        if (list.size >0) {
            weatherMoreDetails(list[0])
            binding.recyclerWeather.setHasFixedSize(true)
            binding.recyclerWeather.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false)

            val adapter = WeatherDetailsAdapter(list,this, object : WeatherMoreDetailsCallback {
                override fun onItemClickWeather(weatherList: WeatherList) {
                    weatherMoreDetails(weatherList)
                }
            })
            // Setting the Adapter with the recyclerview
            binding.recyclerWeather.adapter = adapter
        }
    }


    private fun weatherMoreDetails(weatherList: WeatherList) {
        //binding.windTx.text = (Math.round(((weatherList.wind.speed)*3.6) * 10.0) / 10.0).toString()
        try {
            var distance = (Math.round(((weatherList.wind.speed)*3.6) * 10.0) / 10.0)
            binding.windTx.text = DecimalFormat("#.#").format(distance)
             binding.percentTx3.text = "Km/h"

//            val PreferenceManagerClass = PreferenceManagerClass(this)
//            if (PreferenceManagerClass.getBoolean(AppConstants.DISTANCE_UNIT)) {
                //val number2digits: Double = String.format("%.1f", distance).toDouble()
      /*          binding.windTx.text = DecimalFormat("#.#").format(distance)
                binding.percentTx3.text = "Km/h"*/
//            } else {
//                distance = (distance * 0.6214)
//                // val number2digits: Double = String.format("%.1f", distance).toDouble()
//                binding.windTx.text = DecimalFormat("#.#").format(distance)
//                binding.percentTx3.text = "Miles/h"
//            }


/*
            binding.windDegreeTx.text = (weatherList.wind.deg).toString()
            binding.cloudTx.text = (weatherList.clouds.all).toString()
            binding.pressureTx.text = (weatherList.main.pressure*0.1).toString()
            Log.d(TAG, "weatherMoreDetails: "+ DecimalFormat("#.#").format(distance))
*/
            binding.precipitationTx.text = "${((weatherList.pop)*100).toInt()}"
            binding.humidityTx.text = (weatherList.main.humidity.toString())

        } catch (e: Exception) {
            Log.d("weatherMoreDetails", "weatherMoreDetails: $e")
        }

    }

}