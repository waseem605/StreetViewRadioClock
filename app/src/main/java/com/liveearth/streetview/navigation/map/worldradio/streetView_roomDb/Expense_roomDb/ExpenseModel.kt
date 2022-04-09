package com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel
import com.mapbox.mapboxsdk.geometry.LatLng
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "Expense_table")
class ExpenseModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "category")
    var category: String?,
    @ColumnInfo(name = "date")
    var date: String?,
    @ColumnInfo(name = "location")
    var location: String?,
    @TypeConverters(ExpenseItemTypeConverters::class)
    @ColumnInfo(name = "itemList")
    var itemList: List<ExpenseItemModel>?,
    @ColumnInfo(name = "description")
    var description: String?,
    @ColumnInfo(name = "totalExpense")
    var totalExpense: Int?
)