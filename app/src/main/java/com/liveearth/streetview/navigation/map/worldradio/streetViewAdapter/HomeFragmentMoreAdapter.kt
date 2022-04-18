package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.HomeFragmentClickCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.StreetViewNearMeCallBack
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.HomeFragmentModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.Result
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView


class HomeFragmentMoreAdapter(
    private val modelArrayList: ArrayList<HomeFragmentModel>,
    private val context: Context,
    val callBack: HomeFragmentClickCallBack

) : RecyclerView.Adapter<HomeFragmentMoreAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_main_bottom_item, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            holder.itemCardView.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_SECOND_COLOR))
            val model = modelArrayList[position]

            holder.title.text = model.title
            Glide.with(context).load(model.image).into(holder.imageView)
            holder.itemView.setOnClickListener {
                callBack.onItemClickListener(model)
            }
        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.mainItemImage)
        var itemCardView = itemView.findViewById<CardView>(R.id.cardMoreBackground)
        var title = itemView.findViewById<TextView>(R.id.mainItemText)

    }
}