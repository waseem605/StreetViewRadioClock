package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WorldClockCallBack
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewWorldClockBinding
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.roomdatabase.*
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.SavedWorldTimeAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.util.*

@SuppressLint("LogNotTimber")
class StreetViewWorldClockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreetViewWorldClockBinding
    private val TAG = "WorldClockActivity"
    var mCountryCode = ConstantsStreetView.currentCountryCode
    var mCountryName = ConstantsStreetView.currentCountryName
    lateinit var mSavedAdapter: SavedWorldTimeAdapter
    private var mWorldClockList: ArrayList<WorldClockModel> = ArrayList()
    private var mSavedTimZoneList: ArrayList<WordTimeZoneModel> = ArrayList()
    lateinit var mTimeZoneString: String
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewWorldClockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
        val jsonString: String = getdataFromJson()
        parseJsonStringToNewsList(jsonString)
        clickListenerClock()
        getCurrentLocationTimeZone()
        showSavedTimeZones()

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
                    setTimeToClockView(mTimeZoneString, iso, timezone)
                    break
                }
                mWorldClockList.add(WorldClockModel(timezone, iso, country))
            } catch (e: Exception) {
            }
        }
    }

    private fun setTimeToClockView(mTimeZoneString: String, iso: String, timezone: String) {

        try {
            binding.clockView.setTimeZone(mTimeZoneString)
            binding.countryName.text = mCountryName
            binding.countryTimeZone.text = timezone
            binding.currentIme.text = LocationHelper.getCurrentDateTime(this, 3)
            binding.currentDate.text = LocationHelper.getCurrentDateTime(this, 2)
            Glide.with(this).load("https://flagpedia.net/data/flags/normal/${iso}.png")
                .into(binding.currentCountryImage)

        } catch (e: Exception) {
        }


    }


    private fun clickListenerClock() {
        binding.toolbarLt.titleTx.text = getString(R.string.world_clock)

        binding.toolbarLt.backLink.setOnClickListener {
            onBackPressed()
        }

        binding.allTimeZone.setOnClickListener {
            val intent = Intent(this, WordTimeActivity::class.java)
            intent.putExtra(ConstantsStreetView.All_TIME_INTENT, "heHe")
            startActivity(intent)
        }
        binding.addTimeZone.setOnClickListener {
            val intent = Intent(this, WordTimeActivity::class.java)
            intent.putExtra(ConstantsStreetView.All_TIME_INTENT, ConstantsStreetView.Show_ADD_Btn)
            startActivity(intent)
        }
    }

    private fun showSavedTimeZones() {
        val repository = WorldTimeZoneRepository(StreetViewDatabase(this))
        val factory = WorldTimeZoneViewModelFactory(repository)
        val viewModel: WorldTimeZoneViewModel = ViewModelProvider(this, factory).get(
            WorldTimeZoneViewModel::class.java
        )

        viewModel.getAllData().observe(this, {
            it.let {
                mSavedTimZoneList = it as ArrayList<WordTimeZoneModel>


                binding.timeZoneRecyclerView.apply {
                    mSavedAdapter = SavedWorldTimeAdapter(
                        it,
                        this@StreetViewWorldClockActivity,
                        object : WorldClockCallBack {
                            override fun onItemWorldClock(time: String) {
                                binding.clockView.setTimeZone(time)
                            }


                            override fun onClickAddTimeZone(model: WorldClockModel) {
                            }

                        })
                    layoutManager = LinearLayoutManager(this@StreetViewWorldClockActivity)
                    adapter = mSavedAdapter
                }

            }
        })
    }


    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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
        binding.addTimeZone.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.backOne.setColorFilter(Color.parseColor(backgroundColor))
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.singleTimeZone.setTextColor(Color.parseColor(backgroundColor))
    }
}