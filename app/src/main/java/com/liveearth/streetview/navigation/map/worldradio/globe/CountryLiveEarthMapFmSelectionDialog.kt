package com.liveearth.streetview.navigation.map.worldradio.globe

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.DialogCountrySelectionBinding
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.CountryNameInterface
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream


class CountryLiveEarthMapFmSelectionDialog(private var mContext: Context, val callBacks: CountryNameInterface ) :
    Dialog(mContext) {
    private lateinit var binding:DialogCountrySelectionBinding
    val allCountryFMList = ArrayList<AllCountryFMLiveEarthMapFmModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
        binding = DialogCountrySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("RadioProblem","================================dialog country list==")
        val jsonString: String = getdataFromJson()
        parseJsonStringToNewsList(jsonString)
        addDateToRecyclerView(allCountryFMList)
    }

    private fun getdataFromJson(): String {
        var inputString = ""
        try {
            val inputStream: InputStream = mContext.assets.open("all_country_names.json")
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
            val name = objinside.getString("name")
            val iso = objinside.getString("iso")
            try {
               // if (objinside.getString("name").equals(hhhhhhhhhh))
                allCountryFMList.add(AllCountryFMLiveEarthMapFmModel(name, iso))
            } catch (e: Exception) {
            }
        }
    }

    private fun addDateToRecyclerView(allCountriesFMLiveEarthMapFmModel: ArrayList<AllCountryFMLiveEarthMapFmModel>) {
        if (allCountriesFMLiveEarthMapFmModel.size > 0) {
            try {
                val span = 1
                val gridLayoutManager = GridLayoutManager(mContext, span)
                binding.countryRecyclerView.layoutManager = gridLayoutManager
               val adapterRecyclerView1 = AllCountriesLiveEarthMapFmAdapters(allCountriesFMLiveEarthMapFmModel, mContext, object :
                   CountryNameInterface{
                   override fun onCountryName(name: String, iso: String) {
                       callBacks.onCountryName(name,iso)
                   }
               })
                binding.countryRecyclerView.adapter = adapterRecyclerView1
            } catch (e: Exception) {
            }
        }
    }

}