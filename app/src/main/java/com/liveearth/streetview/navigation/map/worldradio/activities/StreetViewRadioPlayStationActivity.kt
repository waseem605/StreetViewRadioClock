package com.liveearth.streetview.navigation.map.worldradio.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.jean.jcplayer.model.JcAudio
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewRadioPlayStationBinding
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.MainOneCountryFMModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.RadioPlayChannelsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.ChanelPositionCallBack

class StreetViewRadioPlayStationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewRadioPlayStationBinding
    private val TAG = "RadioPlayStation"

    private lateinit var mCountryNameSelected:String
    private lateinit var countryNameFlage:String
    private lateinit var mRadioChannelName:String
    private var mRadioPosition = 0
    var mCountriesRadioChannelList = ArrayList<MainOneCountryFMModel>()
    var mPlaylist: ArrayList<JcAudio> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewRadioPlayStationBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    private fun showRadioItemRecycler(mCountriesRadioChannelList: ArrayList<MainOneCountryFMModel>, mRadioPosition: Int) {

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
}