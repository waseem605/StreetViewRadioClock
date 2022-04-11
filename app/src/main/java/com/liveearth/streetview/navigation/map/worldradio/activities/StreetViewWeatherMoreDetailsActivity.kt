package com.liveearth.streetview.navigation.map.worldradio.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WeatherMoreDetailsCallback
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.WeatherList
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewWeatherMoreDetailsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.WeatherDetailsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper
import java.text.DecimalFormat


class StreetViewWeatherMoreDetailsActivity : AppCompatActivity(){
    private lateinit var binding:ActivityStreetViewWeatherMoreDetailsBinding
    private val TAG = "WeatherMoreDetails"
    var mArrayListWeather = ArrayList<WeatherList>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewWeatherMoreDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mArrayListWeather = StreetViewWeatherHelper.arrayListWeather

        chartVewWeater(mArrayListWeather)
        forecastWeather(mArrayListWeather)

    }

    private fun chartVewWeater(mArrayListWeather: ArrayList<WeatherList>) {
        try {
        val mYValue = ArrayList<Entry>()

        mYValue.add(Entry(0f,(StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[0].main.temp).toFloat())))
        mYValue.add(Entry(1f,(StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[7].main.temp).toFloat())))
        mYValue.add(Entry(2f,(StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[14].main.temp).toFloat())))
        mYValue.add(Entry(3f,(StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[23].main.temp).toFloat())))
        mYValue.add(Entry(4f,(StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[31].main.temp).toFloat())))
        mYValue.add(Entry(5f,(StreetViewWeatherHelper.kalvinToCelsius(mArrayListWeather[39].main.temp).toFloat())))

           /* binding.chart1.isDragEnabled = true
            //binding.chart1.setScaleEnabled(false)
            binding.chart1.setScaleEnabled(true);
            val lineDataSet = LineDataSet(mYValue,"Weather")
            lineDataSet.fillAlpha = 110
            lineDataSet.setCircleColor(resources.getColor(R.color.appIconColor))
            lineDataSet.lineWidth = 3f
            lineDataSet.highlightLineWidth
            lineDataSet.valueTextSize = 10f
            lineDataSet.valueTextColor = Color.WHITE
            val mLineDataSet = ArrayList<ILineDataSet>()
            mLineDataSet.add(lineDataSet)
            val mData = LineData(mLineDataSet)
            binding.chart1.data = mData*/
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
                setDrawLabels(true)
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
            color = ContextCompat.getColor(this@StreetViewWeatherMoreDetailsActivity, R.color.appMainColor)
            setDrawFilled(true)
            fillColor = ContextCompat.getColor(this@StreetViewWeatherMoreDetailsActivity, R.color.colorYellow)
        }
        return LineData(dataSet)
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