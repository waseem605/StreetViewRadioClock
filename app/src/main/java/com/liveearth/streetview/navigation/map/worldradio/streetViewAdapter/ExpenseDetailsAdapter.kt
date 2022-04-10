package com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExpenseCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExpenseItemCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseModel


class ExpenseDetailsAdapter(
    private val modelArrayList: ArrayList<ExpenseItemModel>,
    private val context: Context

) : RecyclerView.Adapter<ExpenseDetailsAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_saved_expense_item_details, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]
            holder.nameExpenseDetailsItem.text = model.name
            holder.priceExpenseDetailsItem.text = model.Price.toString()

        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameExpenseDetailsItem:TextView = itemView.findViewById<TextView>(R.id.nameExpenseDetailsItem)
        var priceExpenseDetailsItem:TextView = itemView.findViewById<TextView>(R.id.priceExpenseDetailsItem)

    }
}