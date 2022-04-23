package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewRadioService.CountryMainFMModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class LocationHelperAssistant() {
    companion object {
        val TAG = "LocationHelper"

        var oneCountriesRadioList = ArrayList<CountryMainFMModel>()

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

        fun shareLocation(context: Context,latitude: Double,longitude: Double){
            val uri = "https://www.google.com/maps/?q=${latitude},${longitude}"
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
            context.startActivity(Intent.createChooser(sharingIntent, "Share in..."))
        }

        private fun shareTextOnly(context: Context,text: String) {
            val intent = Intent(Intent.ACTION_SEND)

            // setting type of data shared as text
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")

            // Adding the text to share using putExtra
            intent.putExtra(Intent.EXTRA_TEXT, text)
            context.startActivity(Intent.createChooser(intent, "Share Via"))
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

        fun hideKeyboard(mContext: Context) {
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
            val simpleDateFormat = SimpleDateFormat("E, dd-MMM-yyyy hh:mm:a")
            val dateTime = simpleDateFormat.format(calendar.time).toString()

            val delimiter = " "
            val parts = dateTime.split(delimiter)
            val currentTime = parts[2]
            val currentDate = parts[1]
            val currentDay = parts[0]

            var data = ""
            when (dateTimeType) {
                1 ->
                    data= "$currentTime $currentDate"
                2 ->
                    data = "$currentDay $currentDate"
                3 ->
                    data = currentTime
                4 ->
                    data = currentDay
            }
            return data
        }

        fun getDateTimeFromDateInString(date:Int,dateTimeType:Int):String {
            val simpleDateFormat = SimpleDateFormat("E dd-MMM-yyyy hh:mm:a")
            val dateTime = simpleDateFormat.format(date).toString()
            val delimiter = " "
            val parts = dateTime.split(delimiter)
            val timeReturn = parts[2]
            val dateReturn = parts[1]
            val dayReturn = parts[0]
            var data = ""
            when (dateTimeType) {
                1 ->
                    data= "${timeReturn} $dateReturn"
                2 ->
                    data = "$dayReturn,$dateReturn"
                3 ->
                    data = timeReturn
            }
            return data
        }


        fun shareExpenseData(context: Context,shareTitle:String,shareDate:String,shareDesc:String,shareLocation:String,itemList:ArrayList<ExpenseItemModel>){
            val intent = Intent(Intent.ACTION_SEND)

            var shareItem = ""
            for (i in 0 until itemList.size) {
                val shareItemTemp = itemList[i].name+"    "+itemList[i].Price
                shareItem +="\n$shareItemTemp"
            }
            val shareBody ="Category: $shareTitle \n Date: $shareDate \n Location: $shareLocation \n\n Items: $shareItem \n\n Description: $shareDesc"
            Log.d(TAG, "showDataExpense: $shareBody")
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "getString(R.string.share_subject)")
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            context.startActivity(Intent.createChooser(intent, "getString(R.string.share_using)"))
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