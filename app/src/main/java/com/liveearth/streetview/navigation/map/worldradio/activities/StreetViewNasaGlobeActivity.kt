package com.liveearth.streetview.navigation.map.worldradio.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewNasaGlobeBinding

class StreetViewNasaGlobeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewNasaGlobeBinding
    val webUrlSkyScanner = "file:///android_asset/marsweather.html"


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewNasaGlobeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webViewSkyScanner.settings.javaScriptEnabled = true
        binding.webViewSkyScanner.setWebViewClient(CruiseWebViewClient())
        binding.webViewSkyScanner.loadUrl(webUrlSkyScanner)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.webViewSkyScanner.canGoBack()) {
            binding.webViewSkyScanner.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private inner class CruiseWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(webview: WebView?, url: String?): Boolean {
            webview!!.loadUrl(url!!)
            binding.skyPB.visibility = View.VISIBLE
            return true
        }

        override fun onPageFinished(webview: WebView?, url: String?) {
            super.onPageFinished(webview, url)
            binding.skyPB.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}