package com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack

import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel

interface ExpenseCallBackListener {
    fun onExpenseAdd(model:ExpenseItemModel)
    fun onRemoveItem(model: ExpenseItemModel,pos:Int)
}