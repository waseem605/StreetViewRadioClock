package com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.facebook.ads.AudienceNetworkAds

import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.streetview.map.navigation.live.earthmap.ads.AppOpenSplashAdManagerLiveEarth
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StreetViewMyApp: Application() {

    companion object {
        var gpsArea: StreetViewMyApp? = null

        var appOpenSplashAdManagerLiveEarth: AppOpenSplashAdManagerLiveEarth? = null
        var earthLiveMapAppOpenAdManager: EarthLiveMapAppOpenAdManager? = null
        fun getStr(id: Int): String {
            return gpsArea!!.getString(id)
        }
    }

    override fun onCreate() {
        super.onCreate()
        gpsArea = this
        FirebaseApp.initializeApp(this)
  /*      MobileAds.initialize(this, object : OnInitializationCompleteListener {
            override fun onInitializationComplete(p0: InitializationStatus?) {
                Log.d("ConstantAdsLoadAds:", "onInitializationComplete: Init Admob")
            }
        })*/
/*
        appOpenSplashAdManagerLiveEarth = AppOpenSplashAdManagerLiveEarth(this)
        earthLiveMapAppOpenAdManager = EarthLiveMapAppOpenAdManager(this)


        try {
            AudienceNetworkAds.initialize(this)
        } catch (e: Exception) {
        }*/

        //getDataFromFirebase()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    private fun getDataFromFirebase() {
        Log.i("getDataFromFirebase: ", " getDataFromFirebase ")
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("EarthLiveMap")
        databaseReference.child("ADS_IDS").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val model = dataSnapshot.getValue<MyAdsModel>(
                    MyAdsModel::class.java
                )
                Log.i("getDataFromFirebase: ", " model ")
                if (model != null) {
                    Log.i("getDataFromFirebase: ", " model != null")
                    EarthLiveMapLoadAds.haveGotSnapshot = true
                  /*  EarthLiveMapLoadAds.appid_admob_inApp = model.appid_admob_inApp
                    EarthLiveMapLoadAds.banner_admob_inApp = model.banner_admob_inApp
                    EarthLiveMapLoadAds.interstitial_admob_inApp = model.interstitial_admob_inApp
                    EarthLiveMapLoadAds.native_admob_inApp = model.native_admob_inApp
                    EarthLiveMapLoadAds.app_open_ad_id_admob = model.app_open_admob_inApp
                    EarthLiveMapLoadAds.app_open_splash_ad_id_admob = model.app_open_splash_ad_id_admob*/
                    EarthLiveMapLoadAds.should_show_splash_app_open = model.isShould_show_splash_app_open
                    EarthLiveMapLoadAds.should_show_app_open = model.isShould_show_open_app
                    EarthLiveMapLoadAds.next_ads_time = model.next_ads_time.toLong()
                    EarthLiveMapLoadAds.next_ads_backPress_time = model.next_ads_backPress_time.toLong()
                    EarthLiveMapLoadAds.current_counter = model.current_counter

                    EarthLiveMapLoadAds.ctr_control_value = model.ctr_control_value
                    EarthLiveMapLoadAds.ad_impression_value_key_var = model.ad_impression_value_key_var
                    EarthLiveMapLoadAds.ad_click_value_key_var = model.ad_click_value_key_var

                    EarthLiveMapLoadAds.percentage =
                        ((EarthLiveMapLoadAds.ad_click_value_key_var.toFloat() * 100) / EarthLiveMapLoadAds.ad_impression_value_key_var.toFloat())


                    Log.i("getDataFromFirebase", "next_ads_time: " + model.next_ads_time.toString())
                    Log.i("getDataFromFirebase", "next_ads_time: " + model.current_counter.toString())
                    Log.i("getDataFromFirebase", "next_ads_time: " + model.native_admob_inApp.toString())

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("getDataFromFirebase", databaseError.message)
            }
        })
    }
}