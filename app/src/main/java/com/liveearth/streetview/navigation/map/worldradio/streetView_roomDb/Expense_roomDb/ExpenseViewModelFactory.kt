package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ExpenseViewModelFactory constructor(private val mContext: Context): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            ExpenseViewModel(mContext) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
