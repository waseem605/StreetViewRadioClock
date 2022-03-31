package com.liveearth.streetview.navigation.map.worldradio.activities

import android.R
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WorldClockCallBack
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityWordTimeBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.WorldClockAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("LogNotTimber")
class WordTimeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityWordTimeBinding
    private val TAG = "WordTime"
    private var mWorldClockList:ArrayList<WorldClockModel> = ArrayList()
    lateinit var adapterTimeZones : WorldClockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.currentIme.text = LocationHelper.getCurrentDateTime(this,3)
        binding.currentDate.text  = LocationHelper.getCurrentDateTime(this,2)
      /*  val arrayTimezone = (TimeZone.getAvailableIDs())

        val adapterGender = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, arrayTimezone)
        binding.etGender.setAdapter(adapterGender)
*/

        val jsonString: String = getdataFromJson()
        parseJsonStringToNewsList(jsonString)

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
                mWorldClockList.add(WorldClockModel(timezone, iso,country))
            } catch (e: Exception) {
            }
        }

        worldClockListRecyclerView(mWorldClockList)
    }

    private fun worldClockListRecyclerView(mWorldClockList: ArrayList<WorldClockModel>) {

        mWorldClockList.let {
            Log.d("onBindViewHolder", "size: "+it.size)

            binding.wordClockRecycler.apply {
                adapterTimeZones = WorldClockAdapter(it,this@WordTimeActivity,object :WorldClockCallBack{
                    override fun onItemWorldClock() {

                    }

                })
                layoutManager = GridLayoutManager(this@WordTimeActivity,2)
                setHasFixedSize(true)
                adapter = adapterTimeZones
            }
        }

    }


}