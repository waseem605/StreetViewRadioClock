package com.liveearth.streetview.navigation.map.worldradio.roomdatabase

import android.content.Context
import androidx.room.*
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.FavouriteLocationDao
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.FavouriteLocationModel

@Database(
    entities =  [WordTimeZoneModel::class,FavouriteLocationModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Convertors::class)
abstract class StreetViewDatabase: RoomDatabase() {

    abstract fun getTimeZoneDao():WorldTimeZoneDao
    abstract fun getFavouriteLocationDao():FavouriteLocationDao



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