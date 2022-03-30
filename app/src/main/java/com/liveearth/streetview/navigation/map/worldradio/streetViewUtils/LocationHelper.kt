package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class LocationHelper() {
    companion object {
        val TAG = "LocationHelper"

        var routeIntent = 1
        var routeNaviIntent = 2

        var locationPointsList= ArrayList<Point>()



        fun setZoomMarker(latitude: Double, longitude: Double, mapbox: MapboxMap, zoom: Int) {
            val position = CameraPosition.Builder()
                .target(LatLng(latitude, longitude))
                .zoom(zoom.toDouble())
                .bearing(180.0)
                .tilt(30.0)
                .build()
            mapbox.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(position), 2000
            )
      /*      mapbox.addMarker(
                MarkerOptions().position(LatLng(latitude, longitude)))
*/
        }

        fun set3dMap(
            latitude: Double,
            longitude: Double,
            mapbox: MapboxMap,
        ) {
            val camPosition = CameraPosition.Builder()
                .target(LatLng(latitude, longitude))
                .tilt(60.0)
                .bearing(-10.0)
                .zoom(16.0)
                .build()
            mapbox.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(camPosition), 2500
            )
        }

        suspend fun getAddressFromLatLong(mContext: Context,latitude: Double,longitude: Double):String{
            var address = "Not Found"
            var error="UnKown"
            val geocoderHelper = Geocoder(mContext)
            val addressList: ArrayList<Address>

            if (Geocoder.isPresent()){
                try {
                    addressList = geocoderHelper.getFromLocation(latitude,longitude,1) as ArrayList<Address>
                    if (addressList.size > 0) {
                        address = addressList[0].getAddressLine(0)
                    }
                } catch (e: Exception) {
                    error=e.message.toString()
                }
            }
            Log.d("getAddressFromLatLong", "getAddressFromLatLong: $address")

            return address
        }

        fun midPointLocation(lat1: Double, lon1: Double, lat2: Double, lon2: Double):LatLng {
            var lat1 = lat1
            var lon1 = lon1
            var lat2 = lat2
            val dLon = Math.toRadians(lon2 - lon1)

            //convert to radians
            lat1 = Math.toRadians(lat1)
            lat2 = Math.toRadians(lat2)
            lon1 = Math.toRadians(lon1)
            val Bx = Math.cos(lat2) * Math.cos(dLon)
            val By = Math.cos(lat2) * Math.sin(dLon)
            val lat3 = Math.atan2(
                Math.sin(lat1) + Math.sin(lat2),
                Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By)
            )
            val lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx)
            //print out in degrees
            println(Math.toDegrees(lat3).toString() + " " + Math.toDegrees(lon3))
            val latLng = LatLng(Math.toDegrees(lat3),Math.toDegrees(lon3))
            return latLng
        }

        fun hideKeyboad(mContext: Context) {
            try {
                val imm: InputMethodManager =
                    mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
            } catch (e: Exception) {
            }
        }

        fun calculationByDistance(StartP: LatLng, EndP: LatLng): Double {
            val Radius = 6371 // radius of earth in Km
            val lat1 = StartP.latitude
            val lat2 = EndP.latitude
            val lon1 = StartP.longitude
            val lon2 = EndP.longitude
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + (Math.cos(Math.toRadians(lat1))
                    * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                    * Math.sin(dLon / 2)))
            val c = 2 * Math.asin(Math.sqrt(a))
            val valueResult = Radius * c
            val km = valueResult / 1
            val newFormat = DecimalFormat("####")
            val kmInDec: Int = Integer.valueOf(newFormat.format(km))
            val meter = valueResult % 1000
            val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
            Log.i(
                "Radius Value", "" + valueResult + "   KM  " + kmInDec
                        + " Meter   " + meterInDec
            )
            return Radius * c
        }


        fun getCurrentDateTime(mContext: Context,dateTimeType:Int):String {
            val calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm:a")
            val dateTime = simpleDateFormat.format(calendar.time).toString()

            val delimiter = " "
            val parts = dateTime.split(delimiter)
            val currentTime = parts[1]
            val currentDate = parts[0]
            var data = ""
            when (dateTimeType) {
                1 ->
                    data= "${currentTime} $currentDate"
                2 ->
                    data = currentDate
                3 ->
                    data = currentTime
            }
            return data
        }

        fun getDateTimeFromDateInString(date:Date,dateTimeType:Int):String {
            val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm:a")
            val dateTime = simpleDateFormat.format(date).toString()
            val delimiter = " "
            val parts = dateTime.split(delimiter)
            val timeReturn = parts[1]
            val dateReturn = parts[0]
            var data = ""
            when (dateTimeType) {
                1 ->
                    data= "${timeReturn} $dateReturn"
                2 ->
                    data = dateReturn
                3 ->
                    data = timeReturn
            }
            return data
        }

        fun getTimeStamp(timeinMillies: Long): String? {
            var date: String? = null
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            date = formatter.format(Date(timeinMillies))
            Log.d("timePickerDialog", "onCreate: =====nnew ==============$date")
            return date
        }

    }

}