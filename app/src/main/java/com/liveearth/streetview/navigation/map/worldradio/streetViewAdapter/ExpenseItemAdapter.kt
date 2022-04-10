package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExpenseItemCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel


class ExpenseItemAdapter(
    private val modelArrayList: ArrayList<ExpenseItemModel>,
    private val context: Context,
    val itemCallBack: ExpenseItemCallBackListener

) : RecyclerView.Adapter<ExpenseItemAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_expense_item, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]
            holder.etNameItem.text = model.name
            holder.etItemPrice.text = model.Price.toString()

            holder.removeItem.setOnClickListener {
                itemCallBack.onRemoveItem(model,position)
            }
        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var removeItem:ImageView = itemView.findViewById<ImageView>(R.id.removeItem)
        var etNameItem:TextView = itemView.findViewById<TextView>(R.id.itemExpenseName)
        var etItemPrice:TextView = itemView.findViewById<TextView>(R.id.itemExpensePrice)

    }
}