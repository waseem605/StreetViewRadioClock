package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
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
        try {
            mPreferenceManagerClass = PreferenceManagerClass(this)
            setThemeColor()
            Unit_Is_Miles = mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Miles,false)
            if (Unit_Is_Miles){
                binding.imageSpeedometer.unit = "M/H"
                binding.maxSpeedUnit.text = "M/H"
                binding.minSpeedUnit.text = "M/H"
                binding.avgSpeedUnit.text = "M/H"
                binding.switchUnit.isChecked = true
            }else{
                binding.imageSpeedometer.unit = "Km/H"
                binding.maxSpeedUnit.text = "Km/H"
                binding.minSpeedUnit.text = "Km/H"
                binding.avgSpeedUnit.text = "Km/H"
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
                    binding.imageSpeedometer.unit = "Km/H"
                    binding.maxSpeedUnit.text = "Km/H"
                    binding.minSpeedUnit.text = "Km/H"
                    binding.avgSpeedUnit.text = "Km/H"
                    Unit_Is_Miles = false
                    mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Miles,false)
                    //binding.switchUnit.isChecked = false
                }
            }


        } catch (e: Exception) {
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
                            mSpeedStart = myLocation!!.speed
                            imageSpeedometer!!.speedTo((mSpeedStart * 3.6).toFloat())

                            if (myLocation!!.speed > 0) {
                                   if (Unit_Is_Miles){
                                       mSpeedUnit = "MP/H"
                                       imageSpeedometer!!.speedTo((mSpeedStart*2.23694).toFloat())
                                       mSpeedStart = (mSpeedStart*2.23694).toFloat()
                                   }else{
                                       mSpeedUnit="Km/H"
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
                        }
                    }
                })
            } catch (e: Exception) {
            }

        } else {
            binding.switchUnit.isEnabled = true
            flag = true
            binding.speedBtn.text = resources.getString(R.string.start)
            locationRepository!!.stopLocation()
            try {

                mSpeedEnd = myLocation!!.speed
                if (Unit_Is_Miles){
                    mSpeedUnit = "MP/H"
                    imageSpeedometer!!.speedTo((mSpeedStart*2.23694).toFloat())
                    mSpeedStart = (mSpeedStart*2.23694).toFloat()
                }else{
                    mSpeedUnit="Km/H"
                    imageSpeedometer!!.speedTo((mSpeedStart*3.6).toFloat())
                    mSpeedStart = (mSpeedStart*3.6).toFloat()
                }
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
                binding.imageSpeedometer.withTremble = false
                //imageSpeedometer!!.stop()
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


/*
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
        )*/

   /*     val latLng = LatLng(myLocation!!.latitude, myLocation!!.longitude)
        LiveEarthAddressFromLatLng(
            this,
            latLng,
            object : LiveEarthAddressFromLatLng.GeoTaskCallback {
                override fun onSuccessLocationFetched(fetchedAddress: String?) {
                    stopLocation = fetchedAddress!!
                }

                override fun onFailedLocationFetched() {
                }
            }).execute()*/
    }

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)

        binding.backSpeed.backLayout.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.backSpeed.imageCenter.setColorFilter(Color.parseColor(backgroundColor))
        binding.backSpeed.imageBottom.setColorFilter(Color.parseColor(backgroundColor))
        binding.backSpeed.imageTop.setColorFilter(Color.parseColor(backgroundColor))
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.maxSpeed.setTextColor(Color.parseColor(backgroundColor))
        binding.avgSpeed.setTextColor(Color.parseColor(backgroundColor))
        binding.minimumSpeed.setTextColor(Color.parseColor(backgroundColor))
    }





}