package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewWorldClockBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.util.*

@SuppressLint("LogNotTimber")
class StreetViewWorldClockActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewWorldClockBinding
    private val TAG = "WorldClockActivity"
    var mCountryCode = ConstantsStreetView.currentCountryCode
    var mCountryName = ConstantsStreetView.currentCountryName
    private var mWorldClockList:ArrayList<WorldClockModel> = ArrayList()
    lateinit var mTimeZoneString: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewWorldClockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val jsonString: String = getdataFromJson()
        parseJsonStringToNewsList(jsonString)
        clickListenerClock()
        getCurrentLocationTimeZone()

    }

    private fun getCurrentLocationTimeZone() {
     /*   Log.d(TAG, "setTimeToClockView: =47==location=====$mCountryName==+"+ConstantsStreetView.currentCountryName)

        val repository = LocationRepository(this,object :MyLocationListener{
            override fun onLocationChanged(location: Location) {
                location.let {
                    LiveEarthAddressFromLatLng(this@StreetViewWorldClockActivity, LatLng(it.latitude,it.longitude),object :LiveEarthAddressFromLatLng.GeoTaskCallback{
                        override fun onSuccessLocationFetched(fetchedAddress: String?) {
                            mCountryCode = ConstantsStreetView.currentCountryCode
                            mCountryName = ConstantsStreetView.currentCountryName
                            Log.d(TAG, "setTimeToClockView: ===location=====$mCountryName==")

                          *//*  val jsonString: String = getdataFromJson()
                            parseJsonStringToNewsList(jsonString)*//*

                        }

                        override fun onFailedLocationFetched() {

                        }

                    })

                }
            }
        })
        repository.stopLocation()*/
    }


    private fun getdataFromJson(): String {
        var inputString = ""
        try {
            val inputStream: InputStream = assets.open("clock_street_view.json")
            inputString = inputStream.bufferedReader().use {
                it.readText()
            }
            Log.d("openViewData", inputString)
        } catch (e: Exception) {
            Log.d("openViewData", e.toString())
        }
        return inputString
    }

    private fun parseJsonStringToNewsList(jsonString: String) {
        val newsArray = JSONArray(jsonString)
        Log.i("fms", "SIZE : " + newsArray.length())
        for (i in 0 until newsArray.length()) {
            val objinside = JSONObject(newsArray.get(i).toString())
            val country = objinside.getString("country")
            val timezone = objinside.getString("timezone")
            val iso = objinside.getString("iso")
            try {
                if (country.equals(mCountryName)) {
                    Log.d(TAG, "setTimeToClockView: ===checked=====$iso==")
                    mTimeZoneString = timezone
                    setTimeToClockView(mTimeZoneString, iso,timezone)
                    break
                }
                mWorldClockList.add(WorldClockModel(timezone, iso,country))
            } catch (e: Exception) {
            }
        }
    }

    private fun setTimeToClockView(mTimeZoneString: String, iso: String, timezone: String) {

        try {
            binding.clockView.setTimeZone(mTimeZoneString)
            binding.countryName.text = mCountryName
            binding.countryTimeZone.text = timezone
            binding.currentIme.text = LocationHelper.getCurrentDateTime(this,3)
            binding.currentDate.text  = LocationHelper.getCurrentDateTime(this,2)
            Glide.with(this).load("https://flagpedia.net/data/flags/normal/${iso}.png").into(binding.currentCountryImage)

        } catch (e: Exception) {
        }


    }


    private fun clickListenerClock() {
        binding.toolbarLt.titleTx.text = getString(R.string.world_clock)


        binding.allTimeZone.setOnClickListener {
            val intent = Intent(this,WordTimeActivity::class.java)
            startActivity(intent)
        }
    }
}