package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import android.annotation.SuppressLint
import android.util.Log
import com.liveearth.streetview.navigation.map.worldradio.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@SuppressLint("LogNotTimber")
class StreetViewWeatherHelper() {
    companion object{

        fun kalvinToCelsius(x:Double):Int{
            val temp = x  - 273.15
            val number2digits: Double = (temp * 100.0).roundToInt() / 100.0
            val finalTemper: Double = Math.round(number2digits * 10.0) / 10.0
            return finalTemper.toInt()
        }
        fun kalvinToForenHeat(x:Double):Int{
            val c = x  - 273.15
            val finalTemper = (c * 9 /5 +32)
            return finalTemper.toInt()
        }

        fun setDate(date:String,index:Int):String{

            var simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm:a")
            val cal = Calendar.getInstance()
            //val dateTime =cal.setTime(simpleDateFormat.parse(date))

            val delimiter = " "
            val parts = date.split(delimiter)
            val currentTime = parts[1]
            val currentDate = parts[0]
            if (index == 1){
                return currentDate
            }else{
                return currentTime
            }
        }

        fun toDistance(i: Double): String {
            val distance = DecimalFormat("#.##").format(i)      //decimal place format
            val builder = StringBuilder()
            //builder.append("Distance: ")
            builder.append(distance)
            Log.d("WeatherDetailsAct", "weatherMoreDetails Helper2: $distance =="+builder.toString())
            Log.d("WeatherDetailsAct", "weatherMoreDetails Helper i: "+i.toString())
            //builder.append("Km")
            return builder.toString()
        }

        fun getTimeLongType(timestamp: Long): String? {
            val dv: Long = java.lang.Long.valueOf(timestamp.toString()) * 1000
            val df = Date(dv)
            val vv = SimpleDateFormat("dd-MMM-yy hh:mm a").format(df)
            Log.d("timePickerDialog", "onCreate: ==getTimeLongType===time==============${vv}")

            return vv
        }


        fun getForWeekly(timestamp: Long): String? {
            try {
                val calendar = Calendar.getInstance()
                val tz = TimeZone.getDefault()
                calendar.timeInMillis = timestamp * 1000
                calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
                val sdf = SimpleDateFormat("dd MMM, EEEE")
                val currenTimeZone = calendar.time as Date

                Log.d("timePickerDialog", "onCreate: =====time==============${sdf.format(currenTimeZone)}")

                return sdf.format(currenTimeZone)
            } catch (e: Exception) {
            }
            return ""
        }
/*
        public fun getIcon(b: String): Int {
            var drw: Int? = null
            when (b) {
                "01d" -> drw= R.drawable.icon_weather_01d
                "02d" -> drw= R.drawable.icon_weather_02d
                "03d" -> drw= R.drawable.icon_weather_03d
                "04d" -> drw= R.drawable.icon_weather_04d
                "09d" -> drw= R.drawable.icon_weather_09d
                "10d" -> drw= R.drawable.icon_weather_10d
                "11d" -> drw= R.drawable.icon_weather_11d
                "13d" -> drw= R.drawable.icon_weather_13d
                "50d" -> drw= R.drawable.icon_weather_50d
                "01n" -> drw= R.drawable.icon_weather_01n
                "02n" -> drw= R.drawable.icon_weather_02n
                "03n" -> drw= R.drawable.icon_weather_03n
                "04n" -> drw= R.drawable.icon_weather_04n
                "09n" -> drw= R.drawable.icon_weather_09n
                "10n" -> drw= R.drawable.icon_weather_10n
                "11n" -> drw= R.drawable.icon_weather_11n
                "13n" -> drw= R.drawable.icon_weather_13n
                "50n" -> drw= R.drawable.icon_weather_50n
            }
            return drw!!
        }*/

    }
}