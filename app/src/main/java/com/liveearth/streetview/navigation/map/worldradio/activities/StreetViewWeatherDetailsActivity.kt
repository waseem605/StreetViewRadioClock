package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dummy.apiServices.WeatherAPI
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewWeatherHelper
import com.example.dummy.apiServices.WeatherAPIServices
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.StreetViewWeatherCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm.RepositoryWeather
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm.RetrofitHelper
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm.ViewModelFactory
import com.liveearth.streetview.navigation.map.worldradio.StreeViewApiServices.mvvm.WeatherViewModel
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.WeatherCallBack
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.StreetViewWeatherModel
import com.liveearth.streetview.navigation.map.worldradio.StreetViewWeather.WeatherList
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewWeatherDetailsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.WeatherDaysAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.HomeFragmentModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("LogNotTimber")
class StreetViewWeatherDetailsActivity : BaseStreetViewActivity() {
    private lateinit var binding:ActivityStreetViewWeatherDetailsBinding
    private lateinit var mPreferenceManagerClass:PreferenceManagerClass
    private lateinit var viewModel: WeatherViewModel
    private val TAG = "WeatherDetails"
    private var mLatitude:Double = 0.0
    private var mLongitude:Double = 0.0
    private lateinit var mAdapter:WeatherDaysAdapter

    private var mDaysWeatherList:ArrayList<WeatherList> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityStreetViewWeatherDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPreferenceManagerClass = PreferenceManagerClass(this)

        mLatitude = intent.getDoubleExtra(ConstantsStreetView.OriginLatitude,0.0)
        mLongitude = intent.getDoubleExtra(ConstantsStreetView.OriginLongitude,0.0)

        //mvvmWeatherDetails(mLatitude,mLongitude)

        mainWeatherDetails(mLatitude,mLongitude)


        binding.detailsMore.setOnClickListener {
            val mainIntent = Intent(this,StreetViewWeatherMoreDetailsActivity::class.java)
            startActivity(mainIntent)
        }

        binding.searchLocationEt.text = ConstantsStreetView.CURRENT_ADDRESS

        binding.searchLocationLayout.setOnClickListener {
            val placeOptions =
                PlaceOptions.builder().backgroundColor(resources.getColor(R.color.white))
                    .build(PlaceOptions.MODE_FULLSCREEN)
            val intent = PlaceAutocomplete.IntentBuilder()
                .placeOptions(placeOptions)
                .accessToken(ConstantsStreetView.accessToken)
                .build(this)
            startActivityForResult(intent, 1)
        }

        binding.toolbar.titleTx.text = "Weather"

        binding.toolbar.backLink.setOnClickListener {
            onBackPressed()
        }
    }

    private fun mvvmWeatherDetails(latitude: Double, longitude: Double) {

        val services = RetrofitHelper.getInstance().create(WeatherAPI::class.java)
        val repositoryWeather = RepositoryWeather(services)
        viewModel = ViewModelProvider(this, ViewModelFactory(repositoryWeather,latitude.toString(),longitude.toString())).get(
            WeatherViewModel::class.java)

        viewModel.weather.observe(this, Observer {
            Log.d(TAG, "mvvmWeatherDetails: ====98====="+StreetViewWeatherHelper.kalvinToForenHeat(it.list[0].main.temp).toString())

                StreetViewWeatherHelper.arrayListWeather = it.list as ArrayList<WeatherList>

                val temperatureUnit = mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)
                if (temperatureUnit){
                    binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToForenHeat(it.list[0].main.temp).toString()
                    binding.weatherUnit.text = "F"
                }else{
                    binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToCelsius(it.list[0].main.temp).toString()
                    binding.weatherUnit.text = "C"
                }
                //binding.weatherTemp.text = ""+StreetViewWeatherHelper.kalvinToCelsius(it.list[0].main.temp).toString()
               // Log.d("454545454","==========="+ StreetViewWeatherHelper.kalvinToCelsius(it.list[0].main.temp).toString())
                try {
                    Glide.with(this@StreetViewWeatherDetailsActivity)
                        .load(StreetViewWeatherHelper.getIcon(it.list[0].weather[0].icon))
                        .into(binding.weatherTodayIcon)
                }catch (e: Exception) {
                    println(e)
                }
                binding.todayDate.text = StreetViewWeatherHelper.getWeatherDate(it.list[0].dt.toLong(), 1)
                binding.weatherTodayType.text = it.list[0].weather[0].main.toString()
                nexDaysDetails(it.list as ArrayList<WeatherList>)
            try {
            } catch (e: Exception) {
            }
        })


    }


    private fun mainWeatherDetails(mLatitude: Double, mLongitude: Double) {
        try {
            showProgressDialog(this)
            val weatherResult = WeatherAPIServices(object :StreetViewWeatherCallBack{
                override fun onSuccess(data: StreetViewWeatherModel) {
                    hideProgressDialog(this@StreetViewWeatherDetailsActivity)
                    StreetViewWeatherHelper.arrayListWeather = data.list as ArrayList<WeatherList>

                    val temperatureUnit = mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)
                    if (temperatureUnit){
                        binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToForenHeat(data.list[0].main.temp).toString()
                        binding.weatherUnit.text = "F"
                    }else{
                        binding.weatherTemp.text = StreetViewWeatherHelper.kalvinToCelsius(data.list[0].main.temp).toString()
                        binding.weatherUnit.text = "C"
                    }
                    //binding.weatherTemp.text = ""+StreetViewWeatherHelper.kalvinToCelsius(data.list[0].main.temp).toString()
                    Log.d("454545454","==========="+ StreetViewWeatherHelper.kalvinToCelsius(data.list[0].main.temp).toString())
                    try {
                        Glide.with(this@StreetViewWeatherDetailsActivity)
                            .load(StreetViewWeatherHelper.getIcon(data.list[0].weather[0].icon))
                            .into(binding.weatherTodayIcon)
                    }catch (e: Exception) {
                        println(e)
                    }
                    binding.todayDate.text = StreetViewWeatherHelper.getWeatherDate(data.list[0].dt.toLong(), 1)
                    binding.weatherTodayType.text = data.list[0].weather[0].main.toString()
                    nexDaysDetails(data.list as ArrayList<WeatherList>)
                }

                override fun onFailure(userError: String) {
                    hideProgressDialog(this@StreetViewWeatherDetailsActivity)
                    Log.d("454545454","==64======error=========$userError==========")
                }

                override fun onLoading(loading: Boolean) {

                }
            })

            GlobalScope.launch {
                Dispatchers.IO
                weatherResult.getDataService(mLatitude.toString(),mLongitude.toString())
            }
        } catch (e: Exception) {
        }

    }

    private fun nexDaysDetails(arrayList: ArrayList<WeatherList>) {
        mDaysWeatherList.clear()
        mDaysWeatherList.add(WeatherList(arrayList[7].clouds,arrayList[7].dt,arrayList[7].dt_txt,arrayList[7].main,arrayList[7].pop,arrayList[7].sys,arrayList[7].visibility,arrayList[7].weather,arrayList[7].wind))
        mDaysWeatherList.add(WeatherList(arrayList[14].clouds,arrayList[14].dt,arrayList[14].dt_txt,arrayList[14].main,arrayList[14].pop,arrayList[14].sys,arrayList[14].visibility,arrayList[14].weather,arrayList[14].wind))
        mDaysWeatherList.add(WeatherList(arrayList[23].clouds,arrayList[23].dt,arrayList[23].dt_txt,arrayList[23].main,arrayList[23].pop,arrayList[23].sys,arrayList[23].visibility,arrayList[23].weather,arrayList[23].wind))
        mDaysWeatherList.add(WeatherList(arrayList[31].clouds,arrayList[31].dt,arrayList[31].dt_txt,arrayList[31].main,arrayList[31].pop,arrayList[31].sys,arrayList[31].visibility,arrayList[31].weather,arrayList[31].wind))
        mDaysWeatherList.add(WeatherList(arrayList[39].clouds,arrayList[39].dt,arrayList[39].dt_txt,arrayList[39].main,arrayList[39].pop,arrayList[39].sys,arrayList[39].visibility,arrayList[39].weather,arrayList[39].wind))

        mAdapter = WeatherDaysAdapter(mDaysWeatherList,this,object :WeatherCallBack{
            override fun onItemWeather(model: StreetViewWeatherModel) {

            }

        })

        binding.dayRecycler.apply {
            layoutManager = LinearLayoutManager(this@StreetViewWeatherDetailsActivity)
            adapter = mAdapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val feature = PlaceAutocomplete.getPlace(data)
                    if (feature != null) {
                        if (feature.center() != null) {
                            if (feature.center()!!.coordinates().isNotEmpty()) {
                                mLatitude = feature.center()?.coordinates()!!.get(1)
                                mLongitude = feature.center()?.coordinates()!!.get(0)
                                binding.searchLocationEt.text = feature.text()!!
                                //mvvmWeatherDetails(mLatitude,mLongitude)
                                mainWeatherDetails(mLatitude,mLongitude)
                                Log.d(TAG, "onActivityResult: "+mLatitude+"--"+mLongitude)
                            }
                        }
                    }
                }
            }
        }
    }


}