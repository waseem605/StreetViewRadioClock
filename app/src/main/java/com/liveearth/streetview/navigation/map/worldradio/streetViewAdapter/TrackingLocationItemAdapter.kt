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
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExpenseItemCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.TrackingLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView


class TrackingLocationItemAdapter(
    private val modelArrayList: ArrayList<TrackingLocationModel>,
    private val context: Context,

) : RecyclerView.Adapter<TrackingLocationItemAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_tracking_item_, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]
            holder.latitudeItem.text = "latitude ${model.lati}"
            holder.longitudeItem.text = "longitude ${model.longi}"
            holder.speedTrackingItem.text = "speed ${model.speed}"

        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var latitudeItem:TextView = itemView.findViewById<TextView>(R.id.latitudeItem)
        var longitudeItem:TextView = itemView.findViewById<TextView>(R.id.longitudeItem)
        var speedTrackingItem:TextView = itemView.findViewById<TextView>(R.id.speedTrackingItem)

    }
}