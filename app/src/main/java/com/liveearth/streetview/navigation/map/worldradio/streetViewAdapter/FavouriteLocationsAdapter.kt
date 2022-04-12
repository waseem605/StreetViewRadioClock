package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.FavLocationListener
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FavouriteLocationsAdapter(
    private val modelArrayList: ArrayList<FavouriteLocationModel>,
    private val mContext: Context,
    private val callBack: FavLocationListener

    ) : RecyclerView.Adapter<FavouriteLocationsAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(mContext).inflate(R.layout.sample_favourite_places_item, parent, false)
          return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]

            holder.timeFavouriteItem.text = "${model.time}  ${model.timeZone}"
            holder.NameFavouriteItem.text = model.name
            holder.addressFavouriteItem.text = model.address
            holder.dateFavouriteItem.text = model.date

            holder.shareItem.setOnClickListener {
                callBack.onShareFavLocation(model)
            }
            holder.navigateToItem.setOnClickListener {
                callBack.onNavigateToLocation(model)
            }
            holder.deleteToItem.setOnClickListener {
                callBack.onDeleteFavLocation(model)
            }
        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timeFavouriteItem: TextView = itemView.findViewById<TextView>(R.id.timeFavouriteItem)
        var NameFavouriteItem: TextView = itemView.findViewById<TextView>(R.id.NameFavouriteItem)
        var addressFavouriteItem: TextView = itemView.findViewById<TextView>(R.id.addressFavouriteItem)
        var dateFavouriteItem: TextView = itemView.findViewById<TextView>(R.id.dateFavouriteItem)
        var shareItem: CardView = itemView.findViewById<CardView>(R.id.shareFavouriteItemCard)
        var navigateToItem: CardView = itemView.findViewById<CardView>(R.id.itemNavigationBtnCard)
        var deleteToItem: CardView = itemView.findViewById<CardView>(R.id.itemDeleteBtn)

    }
}