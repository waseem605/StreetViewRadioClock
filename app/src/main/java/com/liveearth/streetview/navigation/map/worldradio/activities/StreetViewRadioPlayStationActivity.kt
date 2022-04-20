package com.liveearth.streetview.navigation.map.worldradio.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.example.jean.jcplayer.model.JcAudio
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewRadioService.CountryMainFMModel
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewRadioPlayStationBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.RadioPlayChannelsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.StreetViewGlobe.ChanelPositionCallBack

class StreetViewRadioPlayStationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewRadioPlayStationBinding
    private val TAG = "RadioPlayStation"

    private lateinit var mCountryNameSelected:String
    private lateinit var countryNameFlage:String
    private lateinit var mRadioChannelName:String
    private var mRadioPosition = 0
    private lateinit var mPreferenceManagerClass:PreferenceManagerClass
    var mCountriesRadioChannelList = ArrayList<CountryMainFMModel>()
    var mPlaylist: ArrayList<JcAudio> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewRadioPlayStationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
        try {
            mCountryNameSelected = intent.getStringExtra(ConstantsStreetView.Radio_Country_Name)!!
            countryNameFlage = intent.getStringExtra(ConstantsStreetView.RADIO_FLAGE)!!
            mRadioChannelName = intent.getStringExtra(ConstantsStreetView.RADIO_CHANNEL_NAME)!!
            mRadioPosition = intent.getIntExtra("RADIO_POSITION",0)

            binding.nameStationRadio.text = mCountryNameSelected

            mCountriesRadioChannelList = LocationHelper.oneCountriesRadioList
            binding.toolbar.titleTx.text = getString(R.string.playing)

        } catch (e: Exception) {
        }

        showRadioItemRecycler(mCountriesRadioChannelList,mRadioPosition)
        playRadioChannel(mRadioPosition)

        binding.toolbar.backLink.setOnClickListener {
            onBackPressed()
        }


    }

    private fun showRadioItemRecycler(mCountriesRadioChannelList: ArrayList<CountryMainFMModel>, mRadioPosition: Int) {

        mCountriesRadioChannelList.let {
            val mRadioPlayAdapter = RadioPlayChannelsAdapter(it,this,object :ChanelPositionCallBack{
                override fun onChanelClick(flage: String, nameCh: String, pos: Int) {
                    playRadioChannel(pos)
                }

            })

            val carouselLayoutManager: CarouselLayoutManager =
                binding.carouselRecyclerview.getCarouselLayoutManager()
            carouselLayoutManager.scrollToPosition(mRadioPosition)
            binding.carouselRecyclerview.apply {
                set3DItem(true)
                setAlpha(true)
                setHasFixedSize(true)
                //layoutManager = carouselLayoutManager.scrollToPosition(mRadioPosition)
                adapter = mRadioPlayAdapter
                scrollToPosition(mRadioPosition)

            }
        }

    }


    private fun playRadioChannel(mRadioPosition: Int) {


        Log.i("jcPlayerRadiozzz", "Play: " + mCountriesRadioChannelList.size)

        if (binding.jcPlayerRadio.isPlaying) {
            binding.jcPlayerRadio.pause()
            Log.i("jcPlayerRadiozzz", ": " + binding.jcPlayerRadio.currentAudio)
        }
        mPlaylist.clear()
        if (mCountriesRadioChannelList.size > 0) {
            for (x in 0..mCountriesRadioChannelList.size - 1) {
                mPlaylist.add(JcAudio.createFromURL(mCountriesRadioChannelList[x].name, mCountriesRadioChannelList[x].urlResolved))
            }

        }

        try {
            if (mPlaylist[mRadioPosition].path!=null && mPlaylist[mRadioPosition].path!="") {
                binding.jcPlayerRadio.playAudio(mPlaylist[mRadioPosition])
            }
        } catch (e: Exception) {
        }
        binding.jcPlayerRadio.initPlaylist(mPlaylist)
        binding.jcPlayerRadio.createNotification(R.drawable.icon_radio)

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
        binding.toolbar.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))

    }
}