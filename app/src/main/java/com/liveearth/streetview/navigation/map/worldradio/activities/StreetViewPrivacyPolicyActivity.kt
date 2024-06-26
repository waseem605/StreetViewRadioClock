package com.liveearth.streetview.navigation.map.worldradio.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.webkit.CookieManager
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewPrivacyPolicyBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import android.webkit.WebSettings
import android.webkit.CookieSyncManager
import android.webkit.ValueCallback


class StreetViewPrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewPrivacyPolicyBinding
       private lateinit var mPreferenceManagerClass:PreferenceManagerClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
       mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
        try {

            CookieSyncManager.createInstance(this)
            val cookieManager: CookieManager = CookieManager.getInstance()
            cookieManager.removeAllCookie()

            cookieManager.removeAllCookies(object :ValueCallback<Boolean>{
                override fun onReceiveValue(value: Boolean?) {
//                    binding.webView.loadUrl("https://zain22213.blogspot.com/2022/04/there-welcome-to-privacy-policy-of.html")
                    binding.webView.loadUrl("https://farhannabi240.blogspot.com/2022/06/satellite-map.html")

                }

            })

            val webSettings: WebSettings = binding.webView.getSettings()
            webSettings.javaScriptEnabled = true

            CookieSyncManager.createInstance(this)
            cookieManager.removeAllCookie()

            //binding.webView.loadUrl("https://www.javatpoint.com/")
            //binding.webView.loadUrl("https://zain22213.blogspot.com/2022/04/there-welcome-to-privacy-policy-of.html")
        } catch (e: Exception) {
        }



    }

    private fun setThemeColor() {
        val backgroundColor =
            mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor =
            mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
    }
}