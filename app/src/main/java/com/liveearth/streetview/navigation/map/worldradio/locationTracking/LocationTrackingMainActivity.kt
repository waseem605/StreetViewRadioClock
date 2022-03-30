package com.liveearth.streetview.navigation.map.worldradio.locationTracking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityLocationTrackingMainBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView.ACTION_PLAY
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView.ACTION_STOP
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView.MY_TIMER_BROADCAST

class LocationTrackingMainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLocationTrackingMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationTrackingMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {

                    val getSpeed  = intent!!.getFloatExtra("speed",0.0f)
                binding.speedTx.text = getSpeed.toString()
                    Log.d("LocationTracking","=======activity==="+getSpeed)

            }
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter(MY_TIMER_BROADCAST))

//        val intentService = Intent(this,LocationTrackingService::class.java)
//        intentService.setAction(ACTION_STOP)
//        startService(intentService)


        binding.speedTx.setOnClickListener {
            val intentService = Intent(this,LocationTrackingService::class.java)
            intentService.action = ACTION_PLAY
            startService(intentService)
        }

        binding.stopBtn.setOnClickListener {
            val intentService = Intent(this, LocationTrackingService::class.java)
            intentService.action = ACTION_STOP
            startService(intentService)
        }
    }




}