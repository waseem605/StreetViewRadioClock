package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WorldClockCallBack
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityWordTimeBinding
import com.liveearth.streetview.navigation.map.worldradio.hilt.CountryNameModel
import com.liveearth.streetview.navigation.map.worldradio.roomdatabase.*
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.WorldClockAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.ArrayList

@SuppressLint("LogNotTimber")
@AndroidEntryPoint
class WordTimeActivity : BaseStreetViewActivity() {
    private lateinit var binding: ActivityWordTimeBinding
    private val TAG = "WordTime"
    private var mWorldClockList: ArrayList<WorldClockModel> = ArrayList()
    lateinit var adapterTimeZones: WorldClockAdapter
    var mCountryName = ConstantsStreetView.currentCountryName
    lateinit var mTimeZoneString: String
    lateinit var mShowAddBtn: String
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass


    @Inject
    @Named("WorldClockModel_list")
    lateinit var mWorldClockListTwo :ArrayList<WorldClockModel>

    @Inject
    @Named("countryNameListJson")
    lateinit var mCountryNameListTwo :ArrayList<CountryNameModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
        Log.d("worldCloclres", "onCreate: =====world clock==="+mWorldClockListTwo.size)
        Log.d("worldCloclres", "onCreate: ===country name====="+mCountryNameListTwo.size)

        try {
            mShowAddBtn = intent.getStringExtra(ConstantsStreetView.All_TIME_INTENT)!!
            if (mShowAddBtn.equals(ConstantsStreetView.Show_ADD_Btn)) {
                binding.addLayout.visibility = View.GONE
            } else {
                binding.addLayout.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
        }

        /*  val arrayTimezone = (TimeZone.getAvailableIDs())
          val adapterGender = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, arrayTimezone)
          binding.etGender.setAdapter(adapterGender)
  */

        worldClockListRecyclerView(mWorldClockListTwo)

/*        val jsonString: String = getdataFromJson()
        parseJsonStringToNewsList(jsonString)*/

        clickListener()

    }

    private fun clickListener() {

        binding.toolbar.titleTx.text = getString(R.string.world_clock)

        binding.toolbar.backLink.setOnClickListener {
            onBackPressed()
        }

        binding.singleTimeZone.setOnClickListener {
            val intent = Intent(this, StreetViewWorldClockActivity::class.java)
            startActivity(intent)
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

    private fun dataSetTimes(mTimeZoneString: String, iso: String, timezone: String) {
        binding.currentIme.text = LocationHelper.getCurrentDateTime(this, 3)
        binding.currentDate.text = LocationHelper.getCurrentDateTime(this, 2)
        binding.toolbar.titleTx.text =
            getString(com.liveearth.streetview.navigation.map.worldradio.R.string.world_clock)
        binding.countryName.text = mCountryName
        binding.countryTimeZone.text = timezone
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
                    dataSetTimes(mTimeZoneString, iso, timezone)
                }
                mWorldClockList.add(WorldClockModel(timezone, iso, country))
            } catch (e: Exception) {
            }
        }

        worldClockListRecyclerView(mWorldClockList)
    }

    private fun worldClockListRecyclerView(mWorldClockList: ArrayList<WorldClockModel>) {

        mWorldClockList?.let {
            Log.d("onBindViewHolder", "size: " + it.size)

            binding.wordClockRecycler.apply {
                adapterTimeZones = WorldClockAdapter(
                    it,
                    this@WordTimeActivity,
                    mShowAddBtn,
                    object : WorldClockCallBack {
                        override fun onItemWorldClock(time: String) {

                        }


                        override fun onClickAddTimeZone(model: WorldClockModel) {
                            addToMyTimeZone(model)
                        }

                    })
                layoutManager = GridLayoutManager(this@WordTimeActivity, 2)
                setHasFixedSize(true)
                adapter = adapterTimeZones
            }
        }

    }

    private fun addToMyTimeZone(model: WorldClockModel) {
        Log.d("onBindViewHolder", "click===========: " + model.timezone)
        val repository = WorldTimeZoneRepository(StreetViewDatabase(this))
        val factory = WorldTimeZoneViewModelFactory(repository)
        val viewModel: WorldTimeZoneViewModel =
            ViewModelProvider(this, factory).get(WorldTimeZoneViewModel::class.java)

        try {
            GlobalScope.launch {
                withContext(Dispatchers.Main){


                    if (viewModel.getDataByTimeZone(model.timezone!!) !=null){
                        setToast(this@WordTimeActivity, "Already Exist ")

                    }else{
                        viewModel.insertTimeZone(
                            WordTimeZoneModel(
                                id = null,
                                model.country,
                                model.iso,
                                model.timezone
                            )
                        )
                        setToast(this@WordTimeActivity, "Added to ")
                    }
                }
            }
        } catch (e: Exception) {
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this,StreetViewWorldClockActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.addTimeZone.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.backOne.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.toolbar.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.allTimeZone.setTextColor(Color.parseColor(backgroundColor))
    }

}