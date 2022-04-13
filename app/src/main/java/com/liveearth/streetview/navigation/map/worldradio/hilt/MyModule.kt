package com.liveearth.streetview.navigation.map.worldradio.hilt

import android.app.Application
import android.util.Log
import com.bumptech.glide.Glide
import com.example.dummy.apiServices.WeatherAPI
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.WorldClockModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyModule {
    private val BASE_URL = "https://api.openweathermap.org/"



    @Provides
    @Singleton
    @Named("world_clock_string")
    fun worldClockListGetting(app:Application): String{
        var inputString = ""
        try {
            val inputStream: InputStream = app.assets.open("clock_street_view.json")
            inputString = inputStream.bufferedReader().use {
                it.readText()
            }
            Log.d("openViewData", inputString)
        } catch (e: Exception) {
            Log.d("openViewData", e.toString())
        }
        return inputString
    }

    @Provides
    @Singleton
    @Named("WorldClockModel_list")
    fun getDataWorldClock( @Named("world_clock_string")jsonString:String): ArrayList<WorldClockModel>{
        val mWorldClockList: ArrayList<WorldClockModel> = ArrayList()

        val newsArray = JSONArray(jsonString)
        Log.i("fms", "SIZE : " + newsArray.length())
        for (i in 0 until newsArray.length()) {
            val objinside = JSONObject(newsArray.get(i).toString())
            val country = objinside.getString("country")
            val timezone = objinside.getString("timezone")
            val iso = objinside.getString("iso")
            try {
                mWorldClockList.add(WorldClockModel(timezone, iso, country))
            } catch (e: Exception) {
            }
        }
        return mWorldClockList
    }

    @Provides
    @Singleton
    @Named("all_country")
    fun getDataFromJsonCountryNameList(app: Application):String{
        var inputString = ""
        try {
            val inputStream: InputStream = app.assets.open("all_country_names.json")
            inputString = inputStream.bufferedReader().use {
                it.readText()
            }
            Log.d("openViewData", inputString)
        } catch (e: Exception) {
            Log.d("openViewData", e.toString())
        }
        return inputString
    }

    @Provides
    @Singleton
    @Named("countryNameListJson")
    fun parseJsonStringToCountryNameList(@Named("all_country")jsonString: String):ArrayList<CountryNameModel>{
        val mCountryNameListTwo: ArrayList<CountryNameModel> = ArrayList()
        val newsArray = JSONArray(jsonString)
        Log.i("fms", "SIZE : " + newsArray.length())
        for (i in 0 until newsArray.length()) {
            val objinside = JSONObject(newsArray.get(i).toString())
            val name = objinside.getString("name")
            val iso = objinside.getString("iso")
            try {
                mCountryNameListTwo.add(CountryNameModel(name, iso))

            } catch (e: Exception) {
            }
        }
        return mCountryNameListTwo
    }


    @Provides
    @Singleton
    @Named("River_StreetView_list")
    fun getRiverStreetView(app:Application): ArrayList<StreetViewModel>{
        val mWorldClockList: ArrayList<StreetViewModel> = ArrayList()
        var inputString = ""
        try {
            val inputStream: InputStream = app.assets.open("river.json")
            inputString = inputStream.bufferedReader().use {
                it.readText()
            }
            Log.d("openViewData", inputString)
        } catch (e: Exception) {
            Log.d("openViewData", e.toString())
        }
        //return inputString
        val newsArray = JSONArray(inputString)
        Log.i("fms", "SIZE : " + newsArray.length())
        for (i in 0 until newsArray.length()) {
            val objInside = JSONObject(newsArray.get(i).toString())
            val name = objInside.getString("name")
            val lat = objInside.getString("lat")
            val long = objInside.getString("long")
            val description = objInside.getString("description")
            val links = objInside.getString("links")
            try {

                mWorldClockList.add(StreetViewModel(name,lat,long,description,links))
            } catch (e: Exception) {
            }
        }
        return mWorldClockList
    }

    @Provides
    @Singleton
    @Named("ThemeParks_StreetView_list")
    fun getThemeParksStreetView(app:Application): ArrayList<StreetViewModel>{
        val mWorldClockList: ArrayList<StreetViewModel> = ArrayList()
        var inputString = ""
        try {
            val inputStream: InputStream = app.assets.open("theme_parks.json")
            inputString = inputStream.bufferedReader().use {
                it.readText()
            }
            Log.d("openViewData", inputString)
        } catch (e: Exception) {
            Log.d("openViewData", e.toString())
        }
        //return inputString
        val newsArray = JSONArray(inputString)
        Log.i("fms", "SIZE : " + newsArray.length())
        for (i in 0 until newsArray.length()) {
            val objInside = JSONObject(newsArray.get(i).toString())
            val name = objInside.getString("name")
            val lat = objInside.getString("lat")
            val long = objInside.getString("long")
            val description = objInside.getString("description")
            val links = objInside.getString("links")
            try {

                mWorldClockList.add(StreetViewModel(name,lat,long,description,links))
            } catch (e: Exception) {
            }
        }
        return mWorldClockList
    }

    @Provides
    @Singleton
    @Named("Mountain_StreetView_list")
    fun getMountainStreetView(app:Application): ArrayList<StreetViewModel>{
        val mWorldClockList: ArrayList<StreetViewModel> = ArrayList()
        var inputString = ""
        try {
            val inputStream: InputStream = app.assets.open("mountains.json")
            inputString = inputStream.bufferedReader().use {
                it.readText()
            }
            Log.d("openViewData", inputString)
        } catch (e: Exception) {
            Log.d("openViewData", e.toString())
        }
        //return inputString
        val newsArray = JSONArray(inputString)
        Log.i("fms", "SIZE : " + newsArray.length())
        for (i in 0 until newsArray.length()) {
            val objInside = JSONObject(newsArray.get(i).toString())
            val name = objInside.getString("name")
            val lat = objInside.getString("lat")
            val long = objInside.getString("long")
            val description = objInside.getString("description")
            val links = objInside.getString("links")
            try {

                mWorldClockList.add(StreetViewModel(name,lat,long,description,links))
            } catch (e: Exception) {
            }
        }
        return mWorldClockList
    }






    /* @Provides
     @Singleton
     //@Named("String1")
     fun ProvideRetrofitInstance() :Retrofit{
         return Retrofit.Builder().baseUrl(BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .build()
     }

     @Provides
     @Singleton
     fun getRetrofitService(retrofit: Retrofit):WeatherAPI{
         return retrofit.create(WeatherAPI::class.java)
     }*/

}
