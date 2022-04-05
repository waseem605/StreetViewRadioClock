package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.StreetViewNearMeCallBack
import com.liveearth.streetview.navigation.map.worldradio.streetViewPlacesNearMe.Result
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.FavouriteLocationViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NearMeLocationsAdapter(
    private val modelArrayList: ArrayList<Result>,
    private val context: Context,
    private val mFavouriteLocationViewModel: FavouriteLocationViewModel,
    val callBack: StreetViewNearMeCallBack

) : RecyclerView.Adapter<NearMeLocationsAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_near_by_places_item, parent, false)
          return ListViewHolder(view)
    }

    @DelicateCoroutinesApi
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

            GlobalScope.launch(Dispatchers.Main) {

                val it = mFavouriteLocationViewModel.getDataById(model.fsq_id)
                if (it != null) {
                    Glide.with(context).load(R.drawable.ic_favourite_icon_filled).into(holder.itemFavouriteImg)
                }else{
                    Glide.with(context).load(R.drawable.ic_favourite_icon).into(holder.itemFavouriteImg)
                }
            }

            holder.itemNavigationBtn.setOnClickListener {
                callBack.onLocationInfo(model)
            }



            holder.itemFavouriteImg.setOnClickListener {
               // Glide.with(context).load(R.drawable.ic_favourite_icon_filled).into(holder.itemFavouriteImg)
                callBack.addToFavouriteLocation(model)
            }
            holder.itemShareImg.setOnClickListener {
                callBack.shareLocation(model)
            }

        } catch (e: Exception) {
        }
    }

    

    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<ImageView>(R.id.mainItemImage)
        var locationName: TextView = itemView.findViewById<TextView>(R.id.mainItemName)
        var mainItemType: TextView = itemView.findViewById<TextView>(R.id.mainItemType)
        var itemNavigationBtn: CardView = itemView.findViewById<CardView>(R.id.itemNavigationBtn)
        var itemShareImg: ImageView = itemView.findViewById<ImageView>(R.id.itemShareImg)
        var itemFavouriteImg: ImageView = itemView.findViewById<ImageView>(R.id.itemFavouriteImg)

    }
}