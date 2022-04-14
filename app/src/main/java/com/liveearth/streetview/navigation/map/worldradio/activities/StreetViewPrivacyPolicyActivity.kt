package com.liveearth.streetview.navigation.map.worldradio.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewPrivacyPolicyBinding

class StreetViewPrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewPrivacyPolicyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            binding.webView.loadUrl("https://www.javatpoint.com/")
        } catch (e: Exception) {
        }
    }
}