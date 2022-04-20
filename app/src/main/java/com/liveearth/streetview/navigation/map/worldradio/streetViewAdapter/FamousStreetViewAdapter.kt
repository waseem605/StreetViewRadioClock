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
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.CategoryStreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView


class FamousStreetViewAdapter(
    private val modelArrayList: ArrayList<CategoryStreetViewModel>,
    private val context: Context,
    val callBack: CategoryStreetViewCallBackListener

) : RecyclerView.Adapter<FamousStreetViewAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_street_view_famous_item, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]

            holder.cardBackItem.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_SECOND_COLOR))
            holder.image87.setColorFilter(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))

            holder.nameItem.text = model.name
           Glide.with(context).load(model.picture).into(holder.ImageItem)
            holder.itemView.setOnClickListener {
                callBack.onClickCategory(model,position)
            }
        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ImageItem = itemView.findViewById<ImageView>(R.id.streetViewImageFamousItem)
        var nameItem = itemView.findViewById<TextView>(R.id.nameFamousItem)
        var cardBackItem = itemView.findViewById<CardView>(R.id.cardBackItem)
        var image87 = itemView.findViewById<ImageView>(R.id.image877)

    }
}