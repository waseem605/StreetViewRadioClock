package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ColorThemeCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ColorModel

class GridColorDialogAdapter(
    private val modelArrayList: ArrayList<ColorModel>,
    private val context: Context,
    val callBack: ColorThemeCallBackListener
) : RecyclerView.Adapter<GridColorDialogAdapter.GridviewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridviewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sample_color_theme_item, parent, false)
        return GridviewHolder(view)
    }

    override fun onBindViewHolder(holder: GridviewHolder, position: Int) {
        val model = modelArrayList[position]
        holder.itemGridView.setBackgroundColor(Color.parseColor(model.color))
        holder.itemView.setOnClickListener {
            callBack.onColorClick(model)
        }
    }

    override fun getItemCount(): Int {
        return modelArrayList.size
    }

    inner class GridviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemGridView = itemView.findViewById<ConstraintLayout>(R.id.colorCardBack)
    }

}