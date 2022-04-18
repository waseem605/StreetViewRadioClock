package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
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
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MainStreetViewCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView


class StreetViewMainAdapter(
    private val modelArrayList: ArrayList<StreetViewModel>,
    private val context: Context,
    val callBack: MainStreetViewCallBackListener

) : RecyclerView.Adapter<StreetViewMainAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.sample_street_view_item, parent, false)
        return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]

            holder.image87.setColorFilter(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
            holder.navigateCard.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
            holder.shareLocationCard.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))

            holder.categoryNameItem.text = model.name
            holder.categoryDescItem.text = model.description
            Glide.with(context).load(model.link).diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        holder.progress_bar.visibility = View.INVISIBLE
                        holder.categoryImageItem.setImageDrawable(resource)
                        return true
                    }

                }).into(holder.categoryImageItem)

              holder.navigateCard.setOnClickListener {
                  callBack.onClickNavigateCategory(model)
              }
              holder.shareLocationCard.setOnClickListener {
                  callBack.onClickShareCategory(model)
              }
        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
        return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryImageItem = itemView.findViewById<ImageView>(R.id.streetViewImageItem)
        var image87 = itemView.findViewById<ImageView>(R.id.image87)
        var categoryNameItem = itemView.findViewById<TextView>(R.id.streetViewNameItem)
        var categoryDescItem = itemView.findViewById<TextView>(R.id.streetViewDescriptionItem)
        var progress_bar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
        var navigateCard = itemView.findViewById<CardView>(R.id.navigateCard)
        var shareLocationCard = itemView.findViewById<CardView>(R.id.shareLocationCard)

    }
}