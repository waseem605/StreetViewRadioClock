package com.example.dummy.apiServices

import android.util.Log
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewWeatherCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WeatherAPIServices(val callBackWeather: StreetViewWeatherCallBack) {
    private val BASE_URL = "https://api.openweathermap.org/"
    private val appId = "5f3e8f117e64c70e303e1af7d02256e5"



    suspend fun getDataService(lat:String,long:String) {

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


        call.enqueue(object :Callback<StreetViewWeatherModel>{
            override fun onResponse(
                call: Call<StreetViewWeatherModel>,
                response: Response<StreetViewWeatherModel>
            ) {
                if (response.isSuccessful) {
                    callBackWeather.onLoading(false)
                    callBackWeather.onSuccess(response.body()!!)
                    Log.d("454545454","========response.body()!!=========$response.body()!!====")
                }else{
                    callBackWeather.onLoading(false)
                    callBackWeather.onFailure(response.errorBody()!!.toString())
                }
            }

            override fun onFailure(call: Call<StreetViewWeatherModel>, t: Throwable) {
                callBackWeather.onLoading(false)
                callBackWeather.onFailure(t.message!!)
            }

        })
    }


    //private val BASE_URL = "https://api.openweathermap.org/"
   // private val appId = "fef304ef226153ff5a6553f569892a4e"


    //for MVVM
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }


}