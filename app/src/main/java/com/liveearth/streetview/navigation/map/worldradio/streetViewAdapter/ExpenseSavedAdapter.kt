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
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseModel


class ExpenseSavedAdapter(
    private val modelArrayList: ArrayList<ExpenseModel>,
    private val context: Context,
    val itemCallBack: ExpenseCallBackListener

) : RecyclerView.Adapter<ExpenseSavedAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
          val view =
              LayoutInflater.from(context).inflate(R.layout.sample_saved_expense_item, parent, false)
          return ListViewHolder(view)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        try {
            val model = modelArrayList[position]
            holder.categoryExpenseItem.text = model.category
            holder.dateExpenseItem.text = model.date
            holder.totalExpenseItem.text = model.totalExpense.toString()
            holder.locationExpenseItem.text = model.location

            holder.itemView.setOnClickListener {
                itemCallBack.onExpenseView(model)
            }

        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
          return modelArrayList.size
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryExpenseItem:TextView = itemView.findViewById<TextView>(R.id.categoryExpenseItem)
        var dateExpenseItem:TextView = itemView.findViewById<TextView>(R.id.dateExpenseItem)
        var totalExpenseItem:TextView = itemView.findViewById<TextView>(R.id.totalExpenseItem)
        var locationExpenseItem:TextView = itemView.findViewById<TextView>(R.id.locationExpenseItem)

    }
}