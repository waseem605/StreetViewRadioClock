package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.centurionnavigation.callBack.LiveEarthAddressFromLatLng
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.github.anastr.speedviewlib.ImageSpeedometer
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewSpeedoMeterBinding
import com.mapbox.mapboxsdk.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
@SuppressLint("LogNotTimber")
class StreetViewSpeedoMeterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreetViewSpeedoMeterBinding

      var myLocation:Location? =null
      var locationRepository:LocationRepository?=null
      var mStartLatLong:String = ""
      var mEndLatLong:String = ""
      var startLocation:String = ""

      var stopLocation:String = ""

      var mSpeedUnit:String = ""
      var mSpeedStart: Float = 0.0F
      var mSpeedEnd: Float = 0.0F
      var mMaxSpeed: Float = 0.0F
      var flag:Boolean = true

        var imageSpeedometer: ImageSpeedometer?=null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewSpeedoMeterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.kiloMeterBtn.isChecked = true
        binding.imageSpeedometer.unit = "KP/H"

        imageSpeedometer = binding.imageSpeedometer


        binding.speedBtn.setOnClickListener {
            toStartLocation()
        }

    }


    private fun toStartLocation() {
        if (flag){
            flag = false
            binding.kiloMeterBtn.isEnabled = false
            binding.milesPerBtn.isEnabled = false

            try {
                locationRepository = LocationRepository(this, object : MyLocationListener {

                    override fun onLocationChanged(location: Location) {
                        myLocation=location
                        Log.d("myLocation", "onCreate:location $myLocation")
                        if (myLocation!=null) {
                            binding.speedBtn.text = resources.getString(R.string.stop_text)
                            binding.speedBtn.setBackgroundColor(Color.parseColor("#F44336"))
                            mSpeedStart = myLocation!!.speed
                            imageSpeedometer!!.speedTo((mSpeedStart*3.6).toFloat())

                            if (myLocation!!.speed>0){
                                if (binding.kiloMeterBtn.isChecked){
                                    mSpeedUnit="KP/H"
                                    imageSpeedometer!!.speedTo((mSpeedStart*3.6).toFloat())
                                    mSpeedStart = (mSpeedStart*3.6).toFloat()
                                }else{
                                    mSpeedUnit = "MP/H"
                                    imageSpeedometer!!.speedTo((mSpeedStart*2.23694).toFloat())
                                    mSpeedStart = (mSpeedStart*2.23694).toFloat()
                                }
                            }
                            if (mSpeedStart>mMaxSpeed){
                                mMaxSpeed = mSpeedStart
                            }
                            val latLng= LatLng(myLocation!!.latitude,myLocation!!.longitude)
                            LiveEarthAddressFromLatLng(this@StreetViewSpeedoMeterActivity,latLng,object : LiveEarthAddressFromLatLng.GeoTaskCallback{
                                override fun onSuccessLocationFetched(fetchedAddress: String?) {
                                    startLocation = fetchedAddress!!
                                }

                                override fun onFailedLocationFetched() {
                                }
                            }).execute()
                            mStartLatLong = "${myLocation!!.latitude.toString()},${myLocation!!.longitude.toString()}"
                            Log.d("myLocation", "onCreate:speed =end===$mSpeedStart========${startLocation}==============")

                        }
                    }
                })
            } catch (e: Exception) {
            }

        }else{
            binding.kiloMeterBtn.isEnabled = true
            binding.milesPerBtn.isEnabled = true
            flag = true
            //binding.speedBtn.setBackgroundColor(Color.parseColor(AppConstants.APP_SELECTED_COLOR))
            binding.speedBtn.text = resources.getString(R.string.start)

            try {
                locationRepository!!.stopLocation()
                val latLng= LatLng(myLocation!!.latitude,myLocation!!.longitude)
                LiveEarthAddressFromLatLng(this,latLng,object : LiveEarthAddressFromLatLng.GeoTaskCallback{
                    override fun onSuccessLocationFetched(fetchedAddress: String?) {
                        stopLocation = fetchedAddress!!
                    }

                    override fun onFailedLocationFetched() {
                    }
                }).execute()

                mSpeedEnd = myLocation!!.speed
                if (binding.kiloMeterBtn.isChecked){
                    imageSpeedometer!!.speedTo((mSpeedEnd*3.6).toFloat())
                    mSpeedEnd = (mSpeedEnd*3.6).toFloat()
                }else{
                    imageSpeedometer!!.speedTo((mSpeedEnd*2.23694).toFloat())
                    mSpeedEnd = (mSpeedEnd*2.23694).toFloat()
                }
                Log.d("myLocation", "onCreate:speed =end===$mSpeedEnd========${stopLocation}==============")
                mEndLatLong = "${myLocation!!.latitude.toString()},${myLocation!!.longitude.toString()}"
                saveSpeedRecord(stopLocation,mEndLatLong,mSpeedUnit)
            } catch (e: Exception) {
            }
        }
    }

    private fun saveSpeedRecord(stopLocation: String, mEndLatLong: String, mSpeedUnit: String) {
/*
        val speedRepository = SpeedRepository(LiveEarthDatabase(this))
        val speedFactory = SpeedViewModelFactory(speedRepository)
        mSpeedViewModel = ViewModelProvider(this,speedFactory).get(SpeedViewModel::class.java)
        val date = LocationHelper.getCurrentDateTime(this,2)
//        val number3digits:Double = String.format("%.3f", mMaxSpeed).toDouble()
//        val number2digits:Double = String.format("%.2f", number3digits).toDouble()
        val maxSpeedFinal:String = DecimalFormat("#.#").format(mMaxSpeed)
        GlobalScope.launch {
            Dispatchers.Main
            mSpeedViewModel!!.insertSpeed(SpeedModel(id = null,startLocation,mStartLatLong,stopLocation,mEndLatLong,maxSpeedFinal.toString(),mSpeedUnit,date))
        }
        Toast.makeText(this,"Saved record", Toast.LENGTH_SHORT).show()
        imageSpeedometer!!.speedTo((0.0).toFloat())
        */
    }

}