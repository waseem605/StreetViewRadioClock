package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.StreetViewNearMeCallBack
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.MainOneCountryFMModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.HomeFragmentModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.Result
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.ChanelPostionCallBack


class RadioChannelsAdapter(
    private val modelArrayList: ArrayList<MainOneCountryFMModel>,
    private val mContext: Context,
    val callBack: ChanelPostionCallBack

) : RecyclerView.Adapter<RadioChannelsAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(mContext).inflate(R.layout.sample_radio_channel_item, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]

            holder.channelItemName.text = model.name
            if (model.favicon == "") {
                Glide.with(mContext).load(R.drawable.fm_navigation).into(holder.channelItemImage)
            } else {
                Glide.with(mContext).load(model.favicon).into(holder.channelItemImage)
            }
            holder.gradientImg.setOnClickListener {
                callBack.onChanelClick(model.favicon,model.name,position)
            }

        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var gradientImg = itemView.findViewById<ConstraintLayout>(R.id.gradientImg)
        var channelItemImage = itemView.findViewById<ImageView>(R.id.channelItemImage)
        var channelItemName = itemView.findViewById<TextView>(R.id.channelItemName)


    }
}