package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.all.documents.files.reader.documentfiles.viewer.ads.NativeStreetViewAds
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewRadioService.CountryMainFMModel
import com.liveearth.streetview.navigation.map.worldradio.StreetViewGlobe.ChanelPositionCallBack
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView


class RadioChannelsAdapter(
    private val modelArrayListMain: ArrayList<CountryMainFMModel>,
    private val mContext: Context,
    val callBack: ChanelPositionCallBack

) : RecyclerView.Adapter<RadioChannelsAdapter.ListViewHolder>() {
    private val typePost: Int = 1
    private val typeAds: Int = 0
    private val empty: Int = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        /*     val view =
                 LayoutInflater.from(mContext).inflate(R.layout.sample_radio_channel_item, parent, false)
             return ListViewHolder(view)*/
        var newLayout: View? = null
        if (viewType == typePost) {
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.sample_radio_channel_item, parent, false)
        } else if (viewType == typeAds) {
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.native_layout_street_view_default, parent, false)
            NativeStreetViewAds.loadWeatherAdmobNative(mContext, newLayout as FrameLayout)
        } else if (viewType == empty) {
            newLayout =
                LayoutInflater.from(parent.context).inflate(R.layout.empty, parent, false)
        }
        return ListViewHolder(newLayout!!)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 7 == 0) return
        val myPostPosition = position - (position / 7 + 1)
        val model = modelArrayListMain[myPostPosition]

        holder.cardBackItemRadio.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_SECOND_COLOR))
        holder.gradientImgBack.setColorFilter(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))

        holder.channelItemName.text = model.name
        if (model.favicon == "") {
            Glide.with(mContext).load(R.drawable.icon_radio).into(holder.channelItemImage)
        } else {
            Glide.with(mContext).load(model.favicon).into(holder.channelItemImage)
        }
        holder.itemView.setOnClickListener {
            callBack.onChanelClick(model.favicon, model.name, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return empty
        } else {
            if (position % 7 == 0) {
                return typeAds
            } else {
                return typePost
            }
        }
    }

    override fun getItemCount(): Int {
//          return modelArrayListMain.size
        val itemCount = modelArrayListMain.size
        return itemCount + ((itemCount / 6) + 1)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var gradientImg = itemView.findViewById<ConstraintLayout>(R.id.gradientImg)
        var channelItemImage = itemView.findViewById<ImageView>(R.id.channelItemImage)
        var channelItemName = itemView.findViewById<TextView>(R.id.channelItemName)
        var cardBackItemRadio = itemView.findViewById<CardView>(R.id.cardBackItemRadio)
        var gradientImgBack = itemView.findViewById<ImageView>(R.id.gradientImgBack)


    }
}