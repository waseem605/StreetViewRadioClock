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

       // var appOpenSplashAdManagerLiveEarth: AppOpenSplashAdManagerLiveEarth? = null
        var appOpenAdManagerStreetViewClock: AppOpenAdManagerStreetViewClock? = null
        fun getStr(id: Int): String {
            return gpsArea!!.getString(id)
        }
    }

    override fun onCreate() {
        super.onCreate()
        gpsArea = this
        FirebaseApp.initializeApp(this)
        MobileAds.initialize(this, object : OnInitializationCompleteListener {
            override fun onInitializationComplete(p0: InitializationStatus?) {
                Log.d("ConstantAdsLoadAds:", "onInitializationComplete: Init Admob")
            }
        })

       // appOpenSplashAdManagerLiveEarth = AppOpenSplashAdManagerLiveEarth(this)
        appOpenAdManagerStreetViewClock = AppOpenAdManagerStreetViewClock(this)

        try {
            AudienceNetworkAds.initialize(this)
        } catch (e: Exception) {
        }

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
                val model = dataSnapshot.getValue<StreetViewAdsModel>(
                    StreetViewAdsModel::class.java
                )
                Log.i("getDataFromFirebase: ", " model ")
                if (model != null) {
                    Log.i("getDataFromFirebase: ", " model != null")
                    LoadAdsStreetViewClock.haveGotSnapshot = true
                    LoadAdsStreetViewClock.appid_admob_inApp = model.appid_admob_inApp
                    LoadAdsStreetViewClock.banner_admob_inApp = model.banner_admob_inApp
                    LoadAdsStreetViewClock.interstitial_admob_inApp = model.interstitial_admob_inApp
                    LoadAdsStreetViewClock.native_admob_inApp = model.native_admob_inApp
                    LoadAdsStreetViewClock.app_open_ad_id_admob = model.app_open_admob_inApp
                    LoadAdsStreetViewClock.should_show_app_open = model.isShould_show_open_app
                    LoadAdsStreetViewClock.next_ads_time = model.next_ads_time.toLong()
                    LoadAdsStreetViewClock.current_counter = model.current_counter

                    Log.i("getDataFromFirebase", "next_ads_time: " + model.next_ads_time.toString())
                    Log.i("getDataFromFirebase", "current_counter: " + model.current_counter.toString())

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("getDataFromFirebase", databaseError.message)
            }
        })
    }
}