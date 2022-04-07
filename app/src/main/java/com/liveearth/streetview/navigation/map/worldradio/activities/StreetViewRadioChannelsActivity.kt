package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewRadioChannelsBinding
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.FmLiveEarthMapFmInterface
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.MainOneCountryFMModel
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.RetrofitFMLiveEarthMapFm
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.RadioChannelsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.ChanelPositionCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class StreetViewRadioChannelsActivity : BaseStreetViewActivity() {
    private lateinit var binding: ActivityStreetViewRadioChannelsBinding
    val TAG = "RadioChannels"
    lateinit var countryName: String
    var mCountriesRadioChannelList = ArrayList<MainOneCountryFMModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewRadioChannelsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        countryName = intent.getStringExtra(ConstantsStreetView.Radio_Country_Name)!!

        """Tranding in ${this.countryName}""".also { binding.radioCountryName.text = it }

        showProgressDialog(this)
        getAllCountryFMListFromApi(countryName)
    }

    private fun getAllCountryFMListFromApi(mCountryName: String) {
        val retrofitFM = RetrofitFMLiveEarthMapFm.getRetrofitFM(this)
        val apiInterface = retrofitFM.create(FmLiveEarthMapFmInterface::class.java)
        val callToApi = apiInterface.getOneCountryFM(mCountryName)
        callToApi.enqueue(object : Callback<List<MainOneCountryFMModel>> {
            override fun onFailure(call: Call<List<MainOneCountryFMModel>>, t: Throwable) {
                Toast.makeText(
                    this@StreetViewRadioChannelsActivity,
                    "Cannot get now.\nPlease Try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<List<MainOneCountryFMModel>>,
                response: Response<List<MainOneCountryFMModel>>
            ) {
                if (response.isSuccessful) {
                    try {
                        hideProgressDialog(this@StreetViewRadioChannelsActivity)
                        mCountriesRadioChannelList.clear()
                        mCountriesRadioChannelList = response.body() as ArrayList
                        showChannelListOfCountry(mCountriesRadioChannelList)

                        LocationHelper.oneCountriesRadioList.clear()
                        LocationHelper.oneCountriesRadioList = mCountriesRadioChannelList

                    } catch (e: Exception) {
                    }
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        hideProgressDialog(this@StreetViewRadioChannelsActivity)
                        Toast.makeText(
                            this@StreetViewRadioChannelsActivity,
                            "Cannot get  now.\nPlease Try again.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }, 1500)
                }
            }
        })
    }

    private fun showChannelListOfCountry(oneCountriesFM: ArrayList<MainOneCountryFMModel>) {

        Log.d(TAG, "showChannelListOfCountry: ========" + mCountriesRadioChannelList.size)
        val radioAdapter =
            RadioChannelsAdapter(oneCountriesFM, this, object : ChanelPositionCallBack {
                override fun onChanelClick(flage: String, nameCh: String, pos: Int) {
                    val radioIntent = Intent(this@StreetViewRadioChannelsActivity,StreetViewRadioPlayStationActivity::class.java)
                    radioIntent.putExtra(ConstantsStreetView.RADIO_FLAGE,flage)
                    radioIntent.putExtra(ConstantsStreetView.Radio_Country_Name,countryName)
                    radioIntent.putExtra(ConstantsStreetView.RADIO_CHANNEL_NAME,nameCh)
                    radioIntent.putExtra("RADIO_POSITION",pos)
                    startActivity(radioIntent)

                }

            })

        binding.channelRecyclerView.apply {
            setHasFixedSize(true)
            Log.d(TAG, "showChannelListOfCountry: ====recycler====")
            layoutManager = GridLayoutManager(this@StreetViewRadioChannelsActivity, 2)
            adapter = radioAdapter
        }

    }
}