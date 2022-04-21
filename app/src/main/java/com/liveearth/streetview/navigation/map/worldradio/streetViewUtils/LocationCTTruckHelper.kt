package com.liveearth.streetview.navigation.map.worldradio.streetViewUtils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.centurionnavigation.dialogs.EnableInternetConnectionRequestDialogueBox
import com.example.centurionnavigation.dialogs.LocationRequestDialogueBox
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExistCallBackListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class LocationCTTruckHelper{

    companion object{

        suspend fun isProviderCTTruckEnabled(mContext: Context){
            Log.i("getCTLocation","isProviderEnabled :")
            val locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                islocationCTTruckPerMisstionProvided(mContext)
            }else{
                withContext(Dispatchers.Main) {
                    showGpsLocationCTTruckEnabledDialog(mContext)
                }
            }
        }

        fun showGpsLocationCTTruckEnabledDialog(mContext:Context) {
            Log.i("getCTLocation", "showGpsLocationEnabledDialog :")
            val gpsEnablerDialog = LocationRequestDialogueBox(mContext,object :ExistCallBackListener{
                override fun onExistClick() {

                }

            })
            gpsEnablerDialog.show()
        }

        suspend fun islocationCTTruckPerMisstionProvided(mContext: Context):Boolean {
            Log.i("getCTLocation","islocationPerMisstionProvided :")
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    try {
                        ActivityCompat.requestPermissions(
                            mContext as Activity,
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ), 1
                        )
                    } catch (e: Exception) {
                    }

                    return false


                }else{
                    return true
                }
            }else{
                return true
            }
        }

        fun internetCTCheck(mContext: Context): Boolean{
          val internet = isInternetCTTruckAvailable(mContext)
            if (!internet){
                val mInternetCheckDialog = EnableInternetConnectionRequestDialogueBox(mContext)
                mInternetCheckDialog.show()
            }
            return internet
        }

         fun isInternetCTTruckAvailable(mContext: Context): Boolean {
            val connectivityManager =
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetworkInfo: NetworkInfo? = null
            try {
                activeNetworkInfo = connectivityManager.activeNetworkInfo
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }


        fun getStreetViewCTTruckCalculatedDate(dateFormat: String?, days: Int): String? {
            val cal = Calendar.getInstance()
            val s = SimpleDateFormat(dateFormat)
            cal.add(Calendar.DAY_OF_YEAR, days)
            return s.format(Date(cal.timeInMillis))
        }

    }

}
