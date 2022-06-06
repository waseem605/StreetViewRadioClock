package com.liveearth.streetview.navigation.map.worldradio.activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.LoadAdsStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WeatherMoreDetailsCallback
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.WeatherList
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewWeatherMoreDetailsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.WeatherDetailsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper
import java.text.DecimalFormat


class StreetViewWeatherMoreDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreetViewWeatherMoreDetailsBinding
    private val TAG = "WeatherMoreDetails"
    var mArrayListWeather = ArrayList<WeatherList>()
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewWeatherMoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPreferenceManagerClass = PreferenceManagerClass(this)
        mArrayListWeather = StreetViewWeatherHelper.arrayListWeather
        setThemeColor()
        chartVewWeater(mArrayListWeather)
        forecastWeather(mArrayListWeather)
        clickListenerMoreDetails()
        initBannerAd()
    }

    private fun clickListenerMoreDetails() {
        binding.toolbarLt.titleTx.text = getString(R.string.weather_details)
        binding.toolbarLt.backLink.setOnClickListener {
            onBackPressed()
        }
    }

    private fun chartVewWeater(mArrayListWeather: ArrayList<WeatherList>) {
        try {
            val mYValue = ArrayList<Entry>()
            val temperatureUnit =
                mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Fahrenheit, false)
            if (temperatureUnit) {
                mYValue.add(
                    Entry(
                        0f,
                        (StreetViewWeatherHelper.kalvinToForenHeat(mArrayListWeather[0].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        1f,
                        (StreetViewWeatherHelper.kalvinToForenHeat(mArrayListWeather[7].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        2f,
                        (StreetViewWeatherHelper.kalvinToForenHeat(mArrayListWeather[14].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        3f,
                        (StreetViewWeatherHelper.kalvinToForenHeat(mArrayListWeather[23].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        4f,
                        (StreetViewWeatherHelper.kalvinToForenHeat(mArrayListWeather[31].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        5f,
                        (StreetViewWeatherHelper.kalvinToForenHeat(mArrayListWeather[39].main.temp)
                            .toFloat())
                    )
                )
            } else {
                mYValue.add(
                    Entry(
                        0f,
                        (StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[0].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        1f,
                        (StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[7].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        2f,
                        (StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[14].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        3f,
                        (StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[23].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        4f,
                        (StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[31].main.temp)
                            .toFloat())
                    )
                )
                mYValue.add(
                    Entry(
                        5f,
                        (StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[39].main.temp)
                            .toFloat())
                    )
                )
            }

            setUpLineChart(mYValue)

        } catch (e: Exception) {
        }

    }


    private fun setUpLineChart(mYValue: ArrayList<Entry>) {
        val lineData = getDataSet(mYValue)
        binding.chart1.apply {
            data = lineData
            description.isEnabled = false
            isDragEnabled = true
            setScaleEnabled(true)
            setTouchEnabled(true)

            legend.isEnabled = false
            axisLeft.apply {
                setDrawLabels(true)
                setDrawGridLines(false)
                setDrawAxisLine(true)
                spaceBottom = 30f
            }
            axisRight.apply {
                setDrawLabels(false)
                setDrawGridLines(true)
                setDrawAxisLine(false)
            }
            xAxis.apply {
                setDrawLabels(false)
                setDrawGridLines(false)
                setDrawAxisLine(false)
            }
            animateXY(700, 1000, Easing.EaseInOutQuad)
        }
    }

    private fun getDataSet(mYValue: ArrayList<Entry>): LineData {
        val entries = mutableListOf<Entry>()
        val dataList = listOf(1, 20, -20, 33, 54, 7, -18, 2)

        dataList.forEachIndexed { index, element ->
            entries.add(Entry(index.toFloat(), element.toFloat()))
        }

        val dataSet = LineDataSet(mYValue, "")
        dataSet.apply {
            setDrawCircles(true)
            valueTextSize = 0f
            lineWidth = 3f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            //color = ContextCompat.getColor(this@StreetViewWeatherMoreDetailsActivity, R.color.appMainColor)
            color = Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR)

            setDrawFilled(true)
            fillColor = ContextCompat.getColor(
                this@StreetViewWeatherMoreDetailsActivity,
                R.color.colorYellow
            )
        }
        return LineData(dataSet)
    }

    private fun forecastWeather(list: ArrayList<WeatherList>) {
        if (list.size > 0) {
            weatherMoreDetails(list[0])
            binding.recyclerWeather.setHasFixedSize(true)
            binding.recyclerWeather.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL, false
            )

            val adapter = WeatherDetailsAdapter(list, this, object : WeatherMoreDetailsCallback {
                override fun onItemClickWeather(weatherList: WeatherList) {
                    weatherMoreDetails(weatherList)
                }
            })
            // Setting the Adapter with the recyclerview
            binding.recyclerWeather.adapter = adapter
        }
    }


    private fun weatherMoreDetails(weatherList: WeatherList) {
        try {
            val distance = (Math.round(((weatherList.wind.speed) * 3.6) * 10.0) / 10.0)

            if (mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Miles, false)) {
                val number2digits: Double = String.format("%.1f", distance).toDouble()
                binding.windTx.text = DecimalFormat("#.#").format(distance)
                binding.percentTx3.text = "Miles/h"
            } else {
                binding.windTx.text = DecimalFormat("#.#").format(distance)
                binding.percentTx3.text = "Km/h"
            }
/*
            binding.windDegreeTx.text = (weatherList.wind.deg).toString()
            binding.cloudTx.text = (weatherList.clouds.all).toString()
            binding.pressureTx.text = (weatherList.main.pressure*0.1).toString()
            Log.d(TAG, "weatherMoreDetails: "+ DecimalFormat("#.#").format(distance))
*/
            binding.precipitationTx.text = "${((weatherList.pop) * 100).toInt()}"
            binding.humidityTx.text = (weatherList.main.humidity.toString())

        } catch (e: Exception) {
            Log.d("weatherMoreDetails", "weatherMoreDetails: $e")
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
        val backgroundColor =
            mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor =
            mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.weatherDetailsBack.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.humidityCard.setCardBackgroundColor(Color.parseColor(backgroundSecondColor))
        binding.precipitationCard.setCardBackgroundColor(Color.parseColor(backgroundSecondColor))
        binding.windCard.setCardBackgroundColor(Color.parseColor(backgroundSecondColor))
    }
}