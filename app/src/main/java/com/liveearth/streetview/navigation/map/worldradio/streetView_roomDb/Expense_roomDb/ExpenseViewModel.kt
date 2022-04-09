package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.liveearth.streetview.navigation.map.worldradio.roomdatabase.StreetViewDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpenseViewModel(mContext: Context) : ViewModel() {

    private val repository = ExpenseRepository(StreetViewDatabase(mContext))

    fun insertExpense(model: ExpenseModel) {
        viewModelScope.launch {
            Dispatchers.IO
            repository.insertExpense(model)
        }
    }

    suspend fun getDataById(id: Int):ExpenseModel? = repository.getDataById(id)

    suspend fun updateExpense(model: ExpenseModel) = repository.updateExpense(model)
    suspend fun deleteExpense(model: ExpenseModel) = repository.deleteExpense(model)

    suspend fun clearExpense() = repository.clearExpense()

    fun getAllData() = repository.getAllData()

    fun stopObsover() {
        onCleared()
    }

    override fun onCleared() {
        super.onCleared()
    }

}