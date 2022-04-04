package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewRadioChannelsBinding
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.FmLiveEarthMapFmInterface
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.MainOneCountryFMModel
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.RetrofitFMLiveEarthMapFm
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.RadioChannelsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.HomeFragmentModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.ChanelPostionCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class StreetViewRadioChannelsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewRadioChannelsBinding
    val TAG = "RadioChannels"
    lateinit var countryName:String
    var oneCountriesFMModel = ArrayList<MainOneCountryFMModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewRadioChannelsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        countryName = intent.getStringExtra(ConstantsStreetView.Radio_Country_Name)!!

        binding.radioCountryName.text = "Tranding in $countryName"

        getAllCountryFMListFromApi(countryName)
    }

    private fun getAllCountryFMListFromApi(mCountryName: String) {
        //binding.progressBarPlayerCenter.visibility=View.VISIBLE
        val retrofitFM = RetrofitFMLiveEarthMapFm.getRetrofitFM(this)
        val apiInterface = retrofitFM.create(FmLiveEarthMapFmInterface::class.java)
        val callToApi = apiInterface.getOneCountryFM(mCountryName)
        callToApi.enqueue(object : Callback<List<MainOneCountryFMModel>> {
            override fun onFailure(call: Call<List<MainOneCountryFMModel>>, t: Throwable) {
                //binding.progressBarPlayerCenter.visibility=View.GONE
                //selectChanel!!.visibility= View.GONE
                Toast.makeText(this@StreetViewRadioChannelsActivity, "Cannot get now.\nPlease Try again later.", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(
                call: Call<List<MainOneCountryFMModel>>,
                response: Response<List<MainOneCountryFMModel>>
            ) {
                if (response.isSuccessful) {
                    oneCountriesFMModel.clear()
                    oneCountriesFMModel = response.body() as ArrayList

                    showChannelListOfCountry(oneCountriesFMModel)
                  /*  if (oneCountriesFMModel != null) {
                        // binding.progressBarPlayerCenter.visibility=View.GONE
                        selectChanel!!.visibility= View.VISIBLE
                        settingListofStation(oneCountriesFMModel)
                    }*/
                } else {
                    Toast.makeText(this@StreetViewRadioChannelsActivity, "Cannot get  now.\nPlease Try again.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showChannelListOfCountry(oneCountriesFM: ArrayList<MainOneCountryFMModel>) {

        Log.d(TAG, "showChannelListOfCountry: ========"+oneCountriesFMModel.size)
        val radioAdapter = RadioChannelsAdapter(oneCountriesFM,this,object :ChanelPostionCallBack{
            override fun onChanelClick(flage: String, nameCh: String, pos: Int) {

            }

        })

        binding.channelRecyclerView.apply {
            setHasFixedSize(true)
            Log.d(TAG, "showChannelListOfCountry: ====recycler====")
            layoutManager = GridLayoutManager(this@StreetViewRadioChannelsActivity,2)
            adapter = radioAdapter

        }

    }
}