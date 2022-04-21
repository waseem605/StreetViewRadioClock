package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivitySplashScreenStreetViewBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewHelperAssistant
import kotlin.random.Random

class SplashScreenStreetViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashScreenStreetViewBinding
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass
    private val TAG = "SplashScreen"
    private var listOfKeys = ArrayList<String>()

    private var  isFirstTime:Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenStreetViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)
        //setThemeColor()

        isFirstTime = mPreferenceManagerClass.getBoolean(ConstantsStreetView.isFirstTime,true)

        if (isFirstTime){
            binding.acceptedLayout.visibility = View.VISIBLE

            mPreferenceManagerClass.putBoolean(ConstantsStreetView.isFirstTime,false)
            mPreferenceManagerClass.putString(ConstantsStreetView.APP_COLOR,"#237157")
            mPreferenceManagerClass.putString(ConstantsStreetView.APP_COLOR_Second,"#CDE6DD")
            ConstantsStreetView.APP_SELECTED_COLOR ="#237157"
            ConstantsStreetView.APP_SELECTED_SECOND_COLOR ="#CDE6DD"

            binding.letsGoBtn.setOnClickListener {
                startActivity(Intent(this,MainActivity::class.java))
            }

        }else{
            binding.acceptedLayout.visibility = View.INVISIBLE
            setThemeColor()
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
            },3000)
        }




        binding.privacyPolicyText.setOnClickListener {
            StreetViewHelperAssistant.appPrivacyPolicy(this)
        }

    }


    private fun fillMyList() {
        listOfKeys.add(ConstantsStreetView.accessToken1)
        listOfKeys.add(ConstantsStreetView.accessToken2)
        listOfKeys.add(ConstantsStreetView.accessToken3)
        listOfKeys.add(ConstantsStreetView.accessToken5)
        listOfKeys.add(ConstantsStreetView.accessToken6)
        listOfKeys.add(ConstantsStreetView.accessToken7)
        listOfKeys.add(ConstantsStreetView.accessToken8)
        listOfKeys.add(ConstantsStreetView.accessToken9)

        listOfKeys.add(ConstantsStreetView.accessToken10)
        listOfKeys.add(ConstantsStreetView.accessToken11)
        listOfKeys.add(ConstantsStreetView.accessToken12)
        listOfKeys.add(ConstantsStreetView.accessToken13)
        listOfKeys.add(ConstantsStreetView.accessToken14)
        listOfKeys.add(ConstantsStreetView.accessToken15)
        listOfKeys.add(ConstantsStreetView.accessToken16)
        listOfKeys.add(ConstantsStreetView.accessToken17)
        listOfKeys.add(ConstantsStreetView.accessToken18)
        listOfKeys.add(ConstantsStreetView.accessToken19)

        listOfKeys.add(ConstantsStreetView.accessToken20)
        listOfKeys.add(ConstantsStreetView.accessToken21)
        listOfKeys.add(ConstantsStreetView.accessToken22)
        listOfKeys.add(ConstantsStreetView.accessToken23)
        listOfKeys.add(ConstantsStreetView.accessToken24)
        listOfKeys.add(ConstantsStreetView.accessToken25)
        listOfKeys.add(ConstantsStreetView.accessToken26)
        listOfKeys.add(ConstantsStreetView.accessToken27)
        listOfKeys.add(ConstantsStreetView.accessToken28)
    }
/*

    private fun getKeyFromCounterCheck(): String {
        var myKey: String? = null
        when (EarthLiveMapLoadAds.current_counter) {
            1.0 -> {
                myKey = ConstantsStreetView.accessToken1
            }
            2.0 -> {
                myKey = ConstantsStreetView.accessToken2
            }
            3.0 -> {
                myKey = ConstantsStreetView.accessToken3
            }
            4.0 -> {
                myKey = ConstantsStreetView.accessToken4
            }
            5.0 -> {
                myKey = ConstantsStreetView.accessToken5
            }
            6.0 -> {
                myKey = ConstantsStreetView.accessToken6
            }
            7.0 -> {
                myKey = ConstantsStreetView.accessToken7
            }
            8.0 -> {
                myKey = ConstantsStreetView.accessToken8
            }
            9.0 -> {
                myKey = ConstantsStreetView.accessToken9
            }
            10.0 -> {
                myKey = ConstantsStreetView.accessToken10
            }
            11.0 -> {
                myKey = ConstantsStreetView.accessToken11
            }
            12.0 -> {
                myKey = ConstantsStreetView.accessToken12
            }
            13.0 -> {
                myKey = ConstantsStreetView.accessToken13
            }
            14.0 -> {
                myKey = ConstantsStreetView.accessToken14
            }
            15.0 -> {
                myKey = ConstantsStreetView.accessToken15
            }
            16.0 -> {
                myKey = ConstantsStreetView.accessToken16
            }
            17.0 -> {
                myKey = ConstantsStreetView.accessToken17
            }
            18.0 -> {
                myKey = ConstantsStreetView.accessToken18
            }
            19.0 -> {
                myKey = ConstantsStreetView.accessToken19
            }
            20.0 -> {
                myKey = ConstantsStreetView.accessToken20
            }
            21.0 -> {
                myKey = ConstantsStreetView.accessToken21
            }
            22.0 -> {
                myKey = ConstantsStreetView.accessToken22
            }
            23.0 -> {
                myKey = ConstantsStreetView.accessToken23
            }
            24.0 -> {
                myKey = ConstantsStreetView.accessToken24
            }
            25.0 -> {
                myKey = ConstantsStreetView.accessToken25
            }
            26.0 -> {
                myKey = ConstantsStreetView.accessToken26
            }
            27.0 -> {
                myKey = ConstantsStreetView.accessToken27
            }
            28.0 -> {
                myKey = ConstantsStreetView.accessToken28
            }
            else -> {
                myKey = listOfKeys[Random.nextInt(listOfKeys.size)]
            }
        }
        return myKey
    }
*/


    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        ConstantsStreetView.APP_SELECTED_SECOND_COLOR = backgroundSecondColor
        ConstantsStreetView.APP_SELECTED_COLOR = backgroundColor
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
    }
}