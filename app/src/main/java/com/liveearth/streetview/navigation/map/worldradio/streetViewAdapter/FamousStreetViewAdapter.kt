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
import androidx.recyclerview.widget.RecyclerView
import com.all.documents.files.reader.documentfiles.viewer.ads.NativeStreetViewAds
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.CategoryStreetViewCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.CategoryStreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView


class FamousStreetViewAdapter(
    private val modelArrayList: ArrayList<CategoryStreetViewModel>,
    private val context: Context,
    val callBack: CategoryStreetViewCallBackListener

) : RecyclerView.Adapter<FamousStreetViewAdapter.ListViewHolder>() {
    private val typePost: Int = 1
    private val typeAds: Int = 0
    private val empty: Int = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
       /*   val view =
              LayoutInflater.from(context).inflate(R.layout.sample_street_view_famous_item, parent, false)
          return ListViewHolder(view)*/
        var newLayout: View? = null
        if (viewType == typePost) {
            newLayout = LayoutInflater.from(parent.context).inflate(R.layout.sample_street_view_famous_item, parent, false)
        } else if (viewType == typeAds) {
            newLayout = LayoutInflater.from(parent.context).inflate(R.layout.native_layout_street_view_default, parent, false)
            NativeStreetViewAds.loadWeatherAdmobNative(context, newLayout as FrameLayout)
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
            if (position % 5 == 0) return
            val myPostPosition = position - (position / 5 + 1)

            val model = modelArrayList[myPostPosition]

            holder.cardBackItem.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_SECOND_COLOR))
            holder.image87.setColorFilter(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))

            holder.nameItem.text = model.name
           Glide.with(context).load(model.picture).into(holder.ImageItem)
            holder.itemView.setOnClickListener {
                callBack.onClickCategory(model,position)
            }
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

    override fun getItemCount(): Int {
//          return modelArrayList.size
        val itemCount = modelArrayList.size
        return itemCount + ((itemCount / 4) + 1)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ImageItem = itemView.findViewById<ImageView>(R.id.streetViewImageFamousItem)
        var nameItem = itemView.findViewById<TextView>(R.id.nameFamousItem)
        var cardBackItem = itemView.findViewById<CardView>(R.id.cardBackItem)
        var image87 = itemView.findViewById<ImageView>(R.id.image877)

    }
}