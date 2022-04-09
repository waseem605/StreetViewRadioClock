package com.liveearth.streetview.navigation.map.worldradio.roomdatabase

import android.content.Context
import androidx.room.*
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseItemTypeConverters
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseMainDao
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationDao
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationModel

@Database(
    entities =  [WordTimeZoneModel::class, FavouriteLocationModel::class,ExpenseModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertors::class,ExpenseItemTypeConverters::class)
abstract class StreetViewDatabase: RoomDatabase() {

    abstract fun getTimeZoneDao():WorldTimeZoneDao
    abstract fun getFavouriteLocationDao(): FavouriteLocationDao
    abstract fun getExpenseDao():ExpenseMainDao



    companion object{
        private const val DB_NAME = "StreetView.db"
        @Volatile private  var instance : StreetViewDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            StreetViewDatabase::class.java,
            DB_NAME
        ).build()

    }
}