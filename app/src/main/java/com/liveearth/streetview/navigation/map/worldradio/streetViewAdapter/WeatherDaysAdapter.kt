package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WeatherCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WorldClockCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.WeatherList
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.HomeFragmentModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper.Companion.getDateTimeFromDateInString
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper.Companion.getForWeekly
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper.Companion.setDate


class WeatherDaysAdapter(
    private val modelArrayList: ArrayList<WeatherList>,
    private val context: Context,
    val callBack: WeatherCallBack

) : RecyclerView.Adapter<WeatherDaysAdapter.ListViewHolder>() {

    private lateinit var mPreferenceManagerClass:PreferenceManagerClass

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_weather_item_week, parent, false)
          return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            mPreferenceManagerClass = PreferenceManagerClass(context)
            val model = modelArrayList[position]
            holder.weatherDayItem.text = getForWeekly(model.dt.toLong())
            holder.weatherDateItem.text = StreetViewWeatherHelper.getWeatherDate(model.dt.toLong(), 1)

            val temperatureUnit = mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)
            if (temperatureUnit){
                holder.weatherTempItem.text = StreetViewWeatherHelper.kalvinToForenHeat(model.main.temp).toString()
                holder.weatherUnitItem.text = "F"
            }else{
                holder.weatherTempItem.text = StreetViewWeatherHelper.kalvinToCelsius(model.main.temp).toString()
                holder.weatherUnitItem.text = "C"
            }

            //holder.weatherTempItem.text = StreetViewWeatherHelper.kalvinToCelsius(model.main.temp).toString()

            Glide.with(context)
                .load(StreetViewWeatherHelper.getIcon(model.weather[0].icon))
                .into(holder.weatherImageItem)

          /*  holder.itemView.setOnClickListener {
                callBack.onLocationInfo(model)
            }*/
        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var weatherImageItem = itemView.findViewById<ImageView>(R.id.weatherImageItem)
        var weatherDayItem = itemView.findViewById<TextView>(R.id.weatherDayItem)
        var weatherDateItem = itemView.findViewById<TextView>(R.id.weatherDateItem)
        var weatherTempItem = itemView.findViewById<TextView>(R.id.weatherTempItem)
        var weatherUnitItem = itemView.findViewById<TextView>(R.id.weatherUnitItem)


    }
}