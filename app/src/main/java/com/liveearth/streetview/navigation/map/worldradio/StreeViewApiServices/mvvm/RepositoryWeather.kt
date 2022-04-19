package com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dummy.apiServices.WeatherAPI
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RepositoryWeather(val service: WeatherAPI) {
    //private val appId = "fef304ef226153ff5a6553f569892a4e"
    private val appId = "a1a7b213f6daca60e02d8b98f5dd32b1"
    private val BASE_URL = "https://api.openweathermap.org/"


    private val weatherLiveData = MutableLiveData<StreetViewWeatherModel>()

    val weatherList : LiveData<StreetViewWeatherModel>
    get() = weatherLiveData


    suspend fun getWeather(lat:String,long:String){

        val client = OkHttpClient.Builder()
        client.connectTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        client.addInterceptor(interceptor)
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
            .create(WeatherAPI::class.java)

        Log.d("454545454","========response.body()!!=========$lat=$long===")

        val call= api.getWeatherDetails(lat,long,appId)


        call.enqueue(object : Callback<StreetViewWeatherModel> {
            override fun onResponse(
                call: Call<StreetViewWeatherModel>,
                response: Response<StreetViewWeatherModel>
            ) {
                if (response.isSuccessful) {
                    weatherLiveData.postValue(response.body())
                    Log.d("454545454","========response.body()!!=========$response.body()!!====")
                }else{
                    Log.d("454545454","========response.body()!!=========${response.body()!!.message}====")
                }
            }

            override fun onFailure(call: Call<StreetViewWeatherModel>, t: Throwable) {
                Log.d("454545454","========response.body()!!=========${t.message}====")

            }

        })


       /* val result = service.getWeatherDetailsMVVM(lat,long,appId)
        if (result !=null){
            weatherLiveData.postValue(result.body())
        }*/
    }




}