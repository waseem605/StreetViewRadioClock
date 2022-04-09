package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseMainDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(model: ExpenseModel)

    @Update
    suspend fun updateExpense(model: ExpenseModel)

    @Delete
    suspend fun deleteExpense(model: ExpenseModel)

    @Query("SELECT * FROM Expense_table WHERE id = :id")
    suspend fun getDataById(id: Int):ExpenseModel?

    @Query("SELECT * FROM Expense_table")
    fun gatAllData():LiveData<List<ExpenseModel>>

    @Query("DELETE FROM Expense_table")
    suspend fun clearExpense()

    @Query("DELETE FROM Expense_table WHERE id = :id")
    suspend fun deleteExpenseById(id: Int)

}