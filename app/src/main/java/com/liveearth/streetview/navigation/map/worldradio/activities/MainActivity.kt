package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityMainBinding
import com.liveearth.streetview.navigation.map.worldradio.locationTracking.LocationTrackingMainActivity
import com.mapbox.mapboxsdk.log.LoggerDefinition
import me.ibrahimsn.lib.OnItemSelectedListener
import me.ibrahimsn.lib.SmoothBottomBar
import java.io.File


class MainActivity : BaseStreetViewActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    private lateinit var navController: NavController
    var pos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        //checkLocationPermission()
        buttonClickListener()

        setupSmoothBottomMenu()
        binding.bottomBar.setOnItemSelectedListener {
            pos = it
           when(it){
               0->{
                   navController.navigate(R.id.homeFragment)
               }
               1->{
                   navController.navigate(R.id.themeFragment)
               }
               2->{
                   navController.navigate(R.id.premiumFragment)
               }
               3->{
                   navController.navigate(R.id.settingFragment)
               }
           }
        }


    }

    private fun buttonClickListener() {

        binding.liveEarthBtn.setOnClickListener {
            startActivity(Intent(this, StreetViewLiveEarthActivity::class.java))
        }

        binding.meetMeMainBtn.setOnClickListener {
            startActivity(Intent(this, StreetViewMeetMeActivity::class.java))
        }
        binding.nasaGlobeBtn.setOnClickListener {
            startActivity(Intent(this, PathsPolygonsLabelsLiveEarthMapFmActivity::class.java))
        }

        binding.worldTimeBtn.setOnClickListener {
            val intent = Intent(this, WordTimeActivity::class.java)
            startActivity(intent)
        }

        binding.nearMeLocations.setOnClickListener {
            startActivity(Intent(this, StreetViewNearByPlacesActivity::class.java))
        }

        binding.travelExpense.setOnClickListener {
            //startActivity(Intent(this, StreetViewTravelExpenseActivity::class.java))
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            val uri = Uri.parse(
                (Environment.getExternalStorageDirectory().path
                        + File.separator) + "/Download/AllVideoDownloaderCT/Insta/" + File.separator
            )
            intent.setDataAndType(uri, "*/*")
            startActivity(Intent.createChooser(intent, "Open folder"))

        }
        binding.navigation.setOnClickListener {
            startActivity(Intent(this, StreetViewSearchNavigationActivity::class.java))
        }
        binding.locationTracking.setOnClickListener {
            startActivity(Intent(this, LocationTrackingMainActivity::class.java))
        }
        binding.speedMeter.setOnClickListener {
            startActivity(Intent(this, StreetViewSpeedoMeterActivity::class.java))
        }



    }


    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.street_bottom_menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val fragmentId = navController.currentDestination!!.id
        if (fragmentId == R.id.homeFragment){
            finish()
            finishAffinity()
        }else{
            navController.navigate(R.id.homeFragment)
            if (pos>0){

            }
        }
    }
}


