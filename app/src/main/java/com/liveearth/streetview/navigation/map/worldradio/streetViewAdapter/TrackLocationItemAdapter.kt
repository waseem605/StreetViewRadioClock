package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextClock
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.CategoryStreetViewCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WorldClockCallBack
import com.liveearth.streetview.navigation.map.worldradio.locationTracking.TrackingLocationCallBackClick
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.CategoryStreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationModel
import java.text.DecimalFormat


class TrackLocationItemAdapter(
    private val modelArrayList: ArrayList<TrackLocationModel>,
    private val context: Context,
    val callBack: TrackingLocationCallBackClick

) : RecyclerView.Adapter<TrackLocationItemAdapter.ListViewHolder>() {
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_saved_track_location_item, parent, false)
          return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            mPreferenceManagerClass = PreferenceManagerClass(context)
            holder.locationBackItem.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_SECOND_COLOR))

            val model = modelArrayList[position]
            holder.trackLocationItem.text = model.date
            holder.trackLocationStartTimeItem.text = model.startTime
            holder.trackLocationEndTimeItem.text = model.time

            if (mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Miles, false)) {
                //mDistance = mDistance * 0.6214
                holder.trackLocationDistanceItem.text = DecimalFormat("#.#").format(model.distance!!*0.6214)
                holder.trackLocationSpeedItem.text = DecimalFormat("#.#").format((model.avgSpeed)!!*0.6214)
                holder.trackLocationDistanceUnit.text = "Miles"
                holder.trackLocationSpeedUnit.text = "Miles/h"
            } else {
                holder.trackLocationDistanceItem.text = DecimalFormat("#.#").format(model.distance)
                holder.trackLocationSpeedItem.text = DecimalFormat("#.#").format(model.avgSpeed)
                holder.trackLocationDistanceUnit.text = "Km"
                holder.trackLocationSpeedUnit.text = "Km/h"
            }


            holder.deleteTrackLocationItem.setOnClickListener {
                callBack.onClickDeleteLocation(model,position)
            }
            holder.itemView.setOnClickListener {
                callBack.onClickItemLocation(model,position)
            }
        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var trackLocationItem = itemView.findViewById<TextView>(R.id.trackLocationItem)
        var locationBackItem = itemView.findViewById<CardView>(R.id.locationBackItem)
        var trackLocationStartTimeItem = itemView.findViewById<TextView>(R.id.trackLocationStartTimeItem)
        var trackLocationEndTimeItem = itemView.findViewById<TextView>(R.id.trackLocationEndTimeItem)
        var trackLocationSpeedItem = itemView.findViewById<TextView>(R.id.trackLocationSpeedItem)
        var trackLocationDistanceItem = itemView.findViewById<TextView>(R.id.trackLocationDistanceItem)
        var trackLocationSpeedUnit = itemView.findViewById<TextView>(R.id.trackLocationSpeedUnit)
        var trackLocationDistanceUnit = itemView.findViewById<TextView>(R.id.trackLocationDistanceUnit)
        var deleteTrackLocationItem = itemView.findViewById<ImageView>(R.id.deleteTrackLocationItem)

    }
}