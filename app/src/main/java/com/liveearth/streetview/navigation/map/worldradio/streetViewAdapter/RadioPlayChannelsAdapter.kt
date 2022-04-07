package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source.MainOneCountryFMModel
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.ChanelPositionCallBack


class RadioPlayChannelsAdapter(
    private val modelArrayList: ArrayList<MainOneCountryFMModel>,
    private val mContext: Context,
    val callBack: ChanelPositionCallBack

) : RecyclerView.Adapter<RadioPlayChannelsAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(mContext).inflate(R.layout.sample_radio_play_station_item, parent, false)
          return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]
            holder.channelNamePlay.text = model.name
            if (model.favicon == "") {
                Glide.with(mContext).load(R.drawable.icon_radio).into(holder.channelItemImagePlay)
            } else {
                Glide.with(mContext).load(model.favicon).into(holder.channelItemImagePlay)
            }

            holder.itemView.setOnClickListener {
                callBack.onChanelClick(model.favicon,model.name,position)
            }

        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var channelItemImagePlay = itemView.findViewById<ImageView>(R.id.channelItemImagePlay)
        var channelNamePlay = itemView.findViewById<TextView>(R.id.channelNamePlay)

    }
}