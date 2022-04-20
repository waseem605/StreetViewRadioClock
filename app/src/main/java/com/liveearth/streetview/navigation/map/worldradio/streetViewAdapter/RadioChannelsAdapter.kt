package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.graphics.Color
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
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewRadioService.CountryMainFMModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.StreetViewGlobe.ChanelPositionCallBack


class RadioChannelsAdapter(
    private val modelArrayListMain: ArrayList<CountryMainFMModel>,
    private val mContext: Context,
    val callBack: ChanelPositionCallBack

) : RecyclerView.Adapter<RadioChannelsAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(mContext).inflate(R.layout.sample_radio_channel_item, parent, false)
          return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayListMain[position]

            holder.cardBackItemRadio.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_SECOND_COLOR))
            holder.gradientImgBack.setColorFilter(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))

            holder.channelItemName.text = model.name
            if (model.favicon == "") {
                Glide.with(mContext).load(R.drawable.icon_radio).into(holder.channelItemImage)
            } else {
                Glide.with(mContext).load(model.favicon).into(holder.channelItemImage)
            }
            holder.itemView.setOnClickListener {
                callBack.onChanelClick(model.favicon,model.name,position)
            }

        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayListMain.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var gradientImg = itemView.findViewById<ConstraintLayout>(R.id.gradientImg)
        var channelItemImage = itemView.findViewById<ImageView>(R.id.channelItemImage)
        var channelItemName = itemView.findViewById<TextView>(R.id.channelItemName)
        var cardBackItemRadio = itemView.findViewById<CardView>(R.id.cardBackItemRadio)
        var gradientImgBack = itemView.findViewById<ImageView>(R.id.gradientImgBack)


    }
}