package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb

import androidx.lifecycle.LiveData
import com.liveearth.streetview.navigation.map.worldradio.roomdatabase.StreetViewDatabase

class ExpenseRepository(
    private val mDatabase: StreetViewDatabase
) {
    suspend fun insertExpense(model: ExpenseModel) =
        mDatabase.getExpenseDao().insertExpense(model)

    suspend fun updateExpense(model: ExpenseModel) =
        mDatabase.getExpenseDao().updateExpense(model)

    suspend fun getDataById(id: Int): ExpenseModel? =
        mDatabase.getExpenseDao().getDataById(id)

    suspend fun deleteExpense(model: ExpenseModel) =
        mDatabase.getExpenseDao().deleteExpense(model)

    suspend fun clearExpense() = mDatabase.getExpenseDao().clearExpense()

    fun getAllData(): LiveData<List<ExpenseModel>> = mDatabase.getExpenseDao().gatAllData()

    suspend fun deleteExpenseById(id: Int) = mDatabase.getExpenseDao().deleteExpenseById(id)
}