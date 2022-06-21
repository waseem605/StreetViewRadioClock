package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.all.documents.files.reader.documentfiles.viewer.ads.NativeStreetViewAds
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WorldClockCallBack
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView


class WorldClockAdapter(
    private val modelArrayList: ArrayList<WorldClockModel>,
    private val context: Context,
    private val mShowAddBtn:String,
    val callBack: WorldClockCallBack

) : RecyclerView.Adapter<WorldClockAdapter.ListViewHolder>() {
    private val typePost: Int = 1
    private val typeAds: Int = 0
    private val empty: Int = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
/*          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_world_clock_item, parent, false)
          return ListViewHolder(view)*/
        var newLayout: View? = null
        if (viewType == typePost) {
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.sample_world_clock_item, parent, false)
        } else if (viewType == typeAds) {
            newLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.earth_live_map_native_layout_default, parent, false)
            NativeStreetViewAds.loadWeatherAdmobNative(context, newLayout as FrameLayout)
        } else if (viewType == empty) {
            newLayout =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.earth_live_map_empty, parent, false)
        }
        return ListViewHolder(newLayout!!)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        if (position == 0) {
            return
        }
        if (position % 5 == 0) return
        val myPostPosition = position - (position / 5 + 1)
        try {
            val model = modelArrayList[myPostPosition]
            if (mShowAddBtn == ConstantsStreetView.Show_ADD_Btn){
                holder.addTimeZone.visibility = View.VISIBLE
                holder.addTimeZone.setOnClickListener {
                    callBack.onClickAddTimeZone(model)
                }
            }else{
                holder.addTimeZone.visibility = View.GONE
            }

            Log.d("onBindViewHolder", "onBindViewHolder: "+model.country+"==="+model.timezone)
            holder.countryItemName.text = model.country
            holder.countryTimeZoneItem.text = model.timezone
            holder.countryTimeItem.timeZone = model.timezone

           Glide.with(context).load("https://flagpedia.net/data/flags/normal/${model.iso}.png").into(holder.countryFlagItemImg)
          /*  holder.itemView.setOnClickListener {
                callBack.onLocationInfo(model)
            }*/

            holder.addTimeZone.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
            holder.worldClockBackItem.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_SECOND_COLOR))
        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
        val itemCount = modelArrayList.size
        return itemCount + ((itemCount / 4) + 1)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return empty
        } else {
            if (position % 5 == 0) {
                return typeAds
            } else {
                return typePost
            }
        }
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryFlagItemImg = itemView.findViewById<ImageView>(R.id.countryFlagItemImg)
        var addTimeZone = itemView.findViewById<CardView>(R.id.addTimeZone)
        var worldClockBackItem = itemView.findViewById<CardView>(R.id.worldClockBackItem)
        var countryItemName = itemView.findViewById<TextView>(R.id.countryItemName)
        var countryTimeItem = itemView.findViewById<TextClock>(R.id.countryTimeItem)
        var countryTimeZoneItem = itemView.findViewById<TextView>(R.id.countryTimeZoneItem)

    }
}