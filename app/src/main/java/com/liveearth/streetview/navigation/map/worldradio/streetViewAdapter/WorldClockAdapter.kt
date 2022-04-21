package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.graphics.Color
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_world_clock_item, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]

            if (mShowAddBtn.equals(ConstantsStreetView.Show_ADD_Btn)){
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
          return modelArrayList.size
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