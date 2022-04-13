package com.liveearth.streetview.navigation.map.worldradio.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.CategoryStreetViewCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewMainBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.StreetViewMainAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.CategoryStreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class StreetViewMainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewMainBinding
    private val TAG = "StreetViewMain"
    private lateinit var mAdapter:StreetViewMainAdapter
    private var mRiverList =ArrayList<StreetViewModel>()
    private var mGeneralStreetViewList =ArrayList<StreetViewModel>()



    @Inject
    @Named("River_StreetView_list")
    lateinit var mRiverStreetViewList :ArrayList<StreetViewModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val posIntent = intent.getIntExtra(ConstantsStreetView.StreetView_ID,0)

        Log.d(TAG, "onCreate: =======mStreetViewListTwo========="+mRiverStreetViewList.size)

        getStreetViewData(posIntent)
        val jsonString: String = getdataFromJson()
        parseJsonStringToNewsList(jsonString)

    }

    private fun getdataFromJson(): String {
        var inputString = ""
        try {
            val inputStream: InputStream = assets.open("theme_parks.json")
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
            val lat = objinside.getString("lat")
            val long = objinside.getString("long")
            val description = objinside.getString("description")
            val links = objinside.getString("links")
            try {
              /*  if (country.equals(mCountryName)) {
                    Log.d(TAG, "setTimeToClockView: ===checked=====$iso==")
                    mTimeZoneString = timezone
                    dataSetTimes(mTimeZoneString, iso, timezone)
                }*/
                mRiverList.add(StreetViewModel(name,lat,long,description,links))
            } catch (e: Exception) {
            }
        }

        Log.d(TAG, "parseJsonStringToNewsList: "+mRiverList.size)
        binding.toolbarLt.titleTx.text = mRiverList.size.toString()
        //worldClockListRecyclerView(mWorldClockList)
    }

    private fun getStreetViewData(posIntent: Int) {

        when(posIntent){
            0->{
                mGeneralStreetViewList = mRiverStreetViewList
            }
        }

        mAdapter = StreetViewMainAdapter(mGeneralStreetViewList,this,object :CategoryStreetViewCallBackListener{
            override fun onClickCategory(model: CategoryStreetViewModel, pos: Int) {

            }

        })

        binding.streetViewViewPager.adapter = mAdapter
        binding.streetViewViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL




    }
}