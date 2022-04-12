package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.centurionnavigation.callBack.LiveEarthAddressFromLatLng
import com.github.anastr.speedviewlib.ImageSpeedometer
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewSpeedoMeterBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.mapbox.mapboxsdk.geometry.LatLng
import java.text.DecimalFormat

@SuppressLint("LogNotTimber")
class StreetViewSpeedoMeterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreetViewSpeedoMeterBinding
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass

    private var Unit_Is_Miles:Boolean = false
    var myLocation: Location? = null
    var locationRepository: LocationRepository? = null
    var mStartLatLong: String = ""
    var mEndLatLong: String = ""
    var startLocation: String = ""
    var stopLocation: String = ""

    var mSpeedUnit: String = ""
    var mSpeedStart: Float = 0.0F
    var mSpeedEnd: Float = 0.0F
    var mMaxSpeed: Float = 0.0F
    var mMinSpeed: Float = 0.0F
    var mAverageSpeed: Float = 0.0F
    var flag: Boolean = true
    var imageSpeedometer: ImageSpeedometer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewSpeedoMeterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)

        Unit_Is_Miles = mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Miles,false)
        if (Unit_Is_Miles){
            binding.imageSpeedometer.unit = "M/H"
            binding.maxSpeedUnit.text = "M/H"
            binding.minSpeedUnit.text = "M/H"
            binding.avgSpeedUnit.text = "M/H"
            binding.switchUnit.isChecked = true
        }else{
            binding.imageSpeedometer.unit = "KP/H"
            binding.maxSpeedUnit.text = "KP/H"
            binding.minSpeedUnit.text = "KP/H"
            binding.avgSpeedUnit.text = "KP/H"
            binding.switchUnit.isChecked = false
        }

        imageSpeedometer = binding.imageSpeedometer

        binding.speedBtn.setOnClickListener {
            toStartLocation()
        }
        binding.toolbarLt.backLink.setOnClickListener {
            onBackPressed()
        }
        binding.toolbarLt.titleTx.text = "Speedo Meter"

        binding.switchUnit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.imageSpeedometer.unit = "M/H"
                binding.maxSpeedUnit.text = "M/H"
                binding.minSpeedUnit.text = "M/H"
                binding.avgSpeedUnit.text = "M/H"
                Unit_Is_Miles = true
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Miles,true)
                // binding.switchUnit.isChecked = true
            }else{
                binding.imageSpeedometer.unit = "KP/H"
                binding.maxSpeedUnit.text = "KP/H"
                binding.minSpeedUnit.text = "KP/H"
                binding.avgSpeedUnit.text = "KP/H"
                Unit_Is_Miles = false
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Miles,false)
                //binding.switchUnit.isChecked = false
            }
        }

    }


    private fun toStartLocation() {
        if (flag) {
            flag = false
            binding.switchUnit.isEnabled = false
            try {
                locationRepository = LocationRepository(this, object : MyLocationListener {

                    override fun onLocationChanged(location: Location) {
                        myLocation = location
                        Log.d("myLocation", "onCreate:location $myLocation")
                        if (myLocation != null) {
                            binding.speedBtn.text = resources.getString(R.string.stop_text)
                            //binding.speedBtn.setBackgroundColor(Color.parseColor("#F44336"))
                            mSpeedStart = myLocation!!.speed
                            imageSpeedometer!!.speedTo((mSpeedStart * 3.6).toFloat())

                            if (myLocation!!.speed > 0) {
                                   if (Unit_Is_Miles){
                                       mSpeedUnit = "MP/H"
                                       imageSpeedometer!!.speedTo((mSpeedStart*2.23694).toFloat())
                                       mSpeedStart = (mSpeedStart*2.23694).toFloat()
                                   }else{
                                       mSpeedUnit="KP/H"
                                       imageSpeedometer!!.speedTo((mSpeedStart*3.6).toFloat())
                                       mSpeedStart = (mSpeedStart*3.6).toFloat()
                                   }
                            }
                            if (mSpeedStart > mMaxSpeed) {
                                mMaxSpeed = mSpeedStart
                            }
                            mSpeedStart = mMinSpeed

                            if (mSpeedStart<mMinSpeed){
                                mMinSpeed = mSpeedStart
                            }
                            val latLng = LatLng(myLocation!!.latitude, myLocation!!.longitude)
                            LiveEarthAddressFromLatLng(
                                this@StreetViewSpeedoMeterActivity,
                                latLng,
                                object : LiveEarthAddressFromLatLng.GeoTaskCallback {
                                    override fun onSuccessLocationFetched(fetchedAddress: String?) {
                                        startLocation = fetchedAddress!!
                                    }

                                    override fun onFailedLocationFetched() {
                                    }
                                }).execute()
                            mStartLatLong =
                                "${myLocation!!.latitude},${myLocation!!.longitude}"
                            Log.d(
                                "myLocation",
                                "onCreate:speed =end===$mSpeedStart========${startLocation}=============="
                            )
                        }
                    }
                })
            } catch (e: Exception) {
            }

        } else {
            binding.switchUnit.isEnabled = true
            flag = true
            //binding.speedBtn.setBackgroundColor(Color.parseColor(AppConstants.APP_SELECTED_COLOR))
            binding.speedBtn.text = resources.getString(R.string.start)

            try {
                locationRepository!!.stopLocation()
                val latLng = LatLng(myLocation!!.latitude, myLocation!!.longitude)
                LiveEarthAddressFromLatLng(
                    this,
                    latLng,
                    object : LiveEarthAddressFromLatLng.GeoTaskCallback {
                        override fun onSuccessLocationFetched(fetchedAddress: String?) {
                            stopLocation = fetchedAddress!!
                        }

                        override fun onFailedLocationFetched() {
                        }
                    }).execute()

                mSpeedEnd = myLocation!!.speed

                if (Unit_Is_Miles){
                    mSpeedUnit = "MP/H"
                    imageSpeedometer!!.speedTo((mSpeedStart*2.23694).toFloat())
                    mSpeedStart = (mSpeedStart*2.23694).toFloat()
                }else{
                    mSpeedUnit="KP/H"
                    imageSpeedometer!!.speedTo((mSpeedStart*3.6).toFloat())
                    mSpeedStart = (mSpeedStart*3.6).toFloat()
                }

                /*      if (binding.kiloMeterBtn.isChecked){
                          imageSpeedometer!!.speedTo((mSpeedEnd*3.6).toFloat())
                          mSpeedEnd = (mSpeedEnd*3.6).toFloat()
                      }else{
                          imageSpeedometer!!.speedTo((mSpeedEnd*2.23694).toFloat())
                          mSpeedEnd = (mSpeedEnd*2.23694).toFloat()
                      }*/
                Log.d(
                    "myLocation",
                    "onCreate:speed =end===$mSpeedEnd========${stopLocation}=============="
                )

                mAverageSpeed = (mMinSpeed + mMaxSpeed)/2

                if (Unit_Is_Miles){
                    mAverageSpeed = (mAverageSpeed*2.23694).toFloat()
                    mMinSpeed = (mMinSpeed*2.23694).toFloat()
                    mMaxSpeed = (mMaxSpeed*2.23694).toFloat()
                }else{
                    mAverageSpeed = (mAverageSpeed*3.6).toFloat()
                    mMinSpeed = (mMinSpeed*3.6).toFloat()
                    mMaxSpeed = (mMaxSpeed*3.6).toFloat()
                }

                binding.avgSpeed.text = DecimalFormat("#.#").format(mAverageSpeed)
                binding.minimumSpeed.text = DecimalFormat("#.#").format(mMinSpeed)
                binding.maxSpeed.text = DecimalFormat("#.#").format(mMaxSpeed)

                mEndLatLong =
                    "${myLocation!!.latitude},${myLocation!!.longitude}"
                saveSpeedRecord(stopLocation, mEndLatLong, mSpeedUnit)
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
            mSpeedViewModel!!.insertTimeZone(SpeedModel(id = null,startLocation,mStartLatLong,stopLocation,mEndLatLong,maxSpeedFinal.toString(),mSpeedUnit,date))
        }
        Toast.makeText(this,"Saved record", Toast.LENGTH_SHORT).show()
        imageSpeedometer!!.speedTo((0.0).toFloat())
        */
    }

}