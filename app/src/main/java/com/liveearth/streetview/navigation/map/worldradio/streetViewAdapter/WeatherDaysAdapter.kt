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
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.HomeFragmentModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel


class WeatherDaysAdapter(
    private val modelArrayList: ArrayList<HomeFragmentModel>,
    private val context: Context,
    val callBack: WeatherCallBack

) : RecyclerView.Adapter<WeatherDaysAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_weather_item_week, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]

            holder.weatherDayItem.text = model.title


           //Glide.with(context).load("https://flagpedia.net/data/flags/normal/${model.iso}.png").into(holder.countryFlagItemImg)
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
        //var countryFlagItemImg = itemView.findViewById<ImageView>(R.id.countryFlagItemImg)
        var weatherDayItem = itemView.findViewById<TextView>(R.id.weatherDayItem)


    }
}