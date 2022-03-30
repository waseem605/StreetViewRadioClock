package com.liveearth.streetview.navigation.map.worldradio.globe

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.GridLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.DialogCountrySelectionBinding
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.MainOneCountryFMModel
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.ChanelPostionCallBack


class OneCountryChanelDialogs(private var mContext: Context, var chanelList:ArrayList<MainOneCountryFMModel>, val callBacks: ChanelPostionCallBack) :
    Dialog(mContext) {
    private lateinit var binding:DialogCountrySelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(true)
        binding = DialogCountrySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addDateToRecyclerView(chanelList)
    }

    private fun addDateToRecyclerView(allCountriesFMLiveEarthMapFmModel: ArrayList<MainOneCountryFMModel>) {
        if (allCountriesFMLiveEarthMapFmModel.size > 0) {
            try {
                val span = 1
                val gridLayoutManager = GridLayoutManager(mContext, span)
                binding.countryRecyclerView.layoutManager = gridLayoutManager
                val adapterRecyclerView1 = AllChanelLiveEarthMapFmAdapters(allCountriesFMLiveEarthMapFmModel, mContext, object :
                    ChanelPostionCallBack{
                    override fun onChanelClick(flage: String, nameCh: String, pos: Int) {
                        callBacks.onChanelClick(flage,nameCh,pos)
                    }
                })
                binding.countryRecyclerView.adapter = adapterRecyclerView1
            } catch (e: Exception) {
            }
        }
    }

}