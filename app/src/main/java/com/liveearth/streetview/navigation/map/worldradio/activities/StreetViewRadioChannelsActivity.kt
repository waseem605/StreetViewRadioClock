package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewRadioChannelsBinding
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewRadioService.StreetViewFmInterface
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewRadioService.CountryMainFMModel
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewRadioService.StreetViewRetrofitFM
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.RadioChannelsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelperAssistant
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.StreetViewGlobe.ChanelPositionCallBack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class StreetViewRadioChannelsActivity : BaseStreetViewActivity() {
    private lateinit var binding: ActivityStreetViewRadioChannelsBinding
    val TAG = "RadioChannels"
    lateinit var countryName: String
    lateinit var countryCode: String
    var mCountriesRadioChannelList = ArrayList<CountryMainFMModel>()
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewRadioChannelsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()

        try {
            countryName = intent.getStringExtra(ConstantsStreetView.Radio_Country_Name)!!
            countryCode = intent.getStringExtra(ConstantsStreetView.Radio_Country_Code)!!

            """Trending in ${this.countryName}""".also { binding.radioCountryName.text = it }
            val flage="https://flagpedia.net/data/flags/normal/${countryCode}.png"
            Glide.with(this).load(flage).into(binding.countryFlagImage)

            showProgressDialog(this)
            getAllCountryFMListFromApi(countryName)
            binding.backLink.setOnClickListener {
                onBackPressed()
            }
        } catch (e: Exception) {
        }
    }

    private fun getAllCountryFMListFromApi(mCountryName: String) {
        val retrofitFM = StreetViewRetrofitFM.getRetrofitFM(this)
        val apiInterface = retrofitFM.create(StreetViewFmInterface::class.java)
        val callToApi = apiInterface.getOneCountryFM(mCountryName)
        callToApi.enqueue(object : Callback<List<CountryMainFMModel>> {
            override fun onFailure(call: Call<List<CountryMainFMModel>>, t: Throwable) {
                Toast.makeText(
                    this@StreetViewRadioChannelsActivity,
                    "Cannot get now.\nPlease Try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<List<CountryMainFMModel>>,
                response: Response<List<CountryMainFMModel>>
            ) {
                if (response.isSuccessful) {
                    try {
                        hideProgressDialog(this@StreetViewRadioChannelsActivity)
                        mCountriesRadioChannelList.clear()
                        mCountriesRadioChannelList = response.body() as ArrayList
                        showChannelListOfCountry(mCountriesRadioChannelList)

                        LocationHelperAssistant.oneCountriesRadioList.clear()
                        LocationHelperAssistant.oneCountriesRadioList = mCountriesRadioChannelList

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

    private fun showChannelListOfCountry(oneCountriesMainFM: ArrayList<CountryMainFMModel>) {

        Log.d(TAG, "showChannelListOfCountry: ========" + mCountriesRadioChannelList.size)
        val radioAdapter =
            RadioChannelsAdapter(oneCountriesMainFM, this, object : ChanelPositionCallBack {
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

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.background.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.toolbarLt.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.countryFlagImageCard.setCardBackgroundColor(Color.parseColor(backgroundColor))

    }
}