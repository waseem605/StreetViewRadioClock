package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseModel

interface ExpenseCallBackListener {
    fun onExpenseView(model: ExpenseModel)
}