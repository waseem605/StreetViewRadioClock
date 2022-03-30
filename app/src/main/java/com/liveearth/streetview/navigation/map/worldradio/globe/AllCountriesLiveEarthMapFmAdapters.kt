package com.liveearth.streetview.navigation.map.worldradio.globe

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearthmap2021.fmnavigation.routefinder.my_interfaces.CountryNameInterface


class AllCountriesLiveEarthMapFmAdapters(
    var item: ArrayList<AllCountryFMLiveEarthMapFmModel>,
    val mContext: Context,
    val callBacks: CountryNameInterface
) :
    RecyclerView.Adapter<AllCountriesLiveEarthMapFmAdapters.MyViewHolder>() {

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLayoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.one_fm_country_item, null)
        return MyViewHolder(itemLayoutInflater)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = item[position]

        try {
            holder.countryName!!.text = model.name
        } catch (e: Exception) {
        }

        try {
            val flage="https://flagpedia.net/data/flags/normal/${model.iso}.png"
            Log.i("flagezzz",": "+flage)
            Glide.with(mContext).load(flage).into(holder.countryFlag!!)
        } catch (e: Exception) {
        }
        holder.itemView.setOnClickListener {
            callBacks.onCountryName(model.name!!, model.iso!!)
        }

    }

    inner class MyViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryName: TextView? = null
        var countryFlag: ImageView? = null

        init {
            countryName = itemView.findViewById(R.id.countryName)
            countryFlag = itemView.findViewById(R.id.countryFlag)
        }
    }


}