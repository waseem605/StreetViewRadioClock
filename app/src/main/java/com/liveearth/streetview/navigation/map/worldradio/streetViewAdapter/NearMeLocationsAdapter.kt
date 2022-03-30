package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.StreetViewNearMeCallBack
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.Result


class NearMeLocationsAdapter(
    private val modelArrayList: ArrayList<Result>,
    private val context: Context,
    val callBack: StreetViewNearMeCallBack

) : RecyclerView.Adapter<NearMeLocationsAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_near_by_places_item, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]

            holder.locationName.text = model.name
            holder.mainItemType.text = model.categories[0].name
         /*   if (model.location.address !=null) {
                holder.locationAddress.text = model.location.address
            }else if(model.location.formatted_address !=null){
                holder.locationAddress.text = model.location.formatted_address
            }else{
                holder.locationAddress.text = model.location.locality
            }*/
            Glide.with(context).load(model.categories.get(0).icon.prefix+"64.png").into(holder.imageView)
            holder.itemView.setOnClickListener {
                callBack.onLocationInfo(model)
            }
        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.findViewById<ImageView>(R.id.mainItemImage)
        var locationName = itemView.findViewById<TextView>(R.id.mainItemName)
        var mainItemType = itemView.findViewById<TextView>(R.id.mainItemType)

    }
}