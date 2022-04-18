package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WeatherMoreDetailsCallback
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.WeatherList
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper


class WeatherDetailsAdapter(
    private val modelArrayList: ArrayList<WeatherList>,
    private val context: Context,
    val callBack: WeatherMoreDetailsCallback

) : RecyclerView.Adapter<WeatherDetailsAdapter.ListViewHolder>() {
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_weather_all_item, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        mPreferenceManagerClass = PreferenceManagerClass(context)

        holder.weatherAllBack.setBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_SECOND_COLOR))
        val model = modelArrayList[position]
        val weatherTime = StreetViewWeatherHelper.getTimeLongType(model.dt.toLong())
        val delimiter = " "
        val parts = weatherTime!!.split(delimiter)
        val weatherDateOnly = parts[0]
        val weatherTimeOnly = parts[1]
        val weatherAMOnly = parts[2]

        val temperatureUnit = mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)
        if (temperatureUnit){
            holder.weatherItemTemp.text = StreetViewWeatherHelper.kalvinToForenHeat(model.main.temp).toString()
            holder.degreeC.text = "F"
        }else{
            holder.weatherItemTemp.text = StreetViewWeatherHelper.kalvinToCelsius(model.main.temp).toString()
            holder.degreeC.text = "C"
        }

        val temp = StreetViewWeatherHelper.kalvinToCelsius(model.main.temp).toString()
        val iconTemp = StreetViewWeatherHelper.getIcon(model.weather[0].icon)

        holder.weatherTime.text = "$weatherTimeOnly $weatherAMOnly"
        Glide.with(context).load(iconTemp).into(holder.imageView)

        holder.itemView.setOnClickListener {
            callBack.onItemClickWeather(model)
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.weatherItemImg)
        var weatherTime = itemView.findViewById<TextView>(R.id.weatherItemTime)
        var weatherItemTemp = itemView.findViewById<TextView>(R.id.weatherIteTemp)
        var degreeC = itemView.findViewById<TextView>(R.id.degreeC)
        var weatherAllBack = itemView.findViewById<ConstraintLayout>(R.id.weatherAllBack)

    }
}