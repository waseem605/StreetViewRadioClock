package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.centurionnavigation.dialogs.ExitDialogueBoxStreetView
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExistCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityMainBinding
import com.liveearth.streetview.navigation.map.worldradio.locationTracking.LocationTrackingMainActivity
import java.io.File


class MainActivity : BaseStreetViewActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    private lateinit var navController: NavController
    var pos = 0
    var count = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

       // checkLocationPermission()
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
            count++
            if (count > 1){

                finish()
                finishAffinity()
            }else {
                val dialogExist = ExitDialogueBoxStreetView(this,object :ExistCallBackListener{
                    override fun onExistClick() {

                    }

                })
                dialogExist.show()
            }
        }else{
            navController.navigate(R.id.homeFragment)
            binding.bottomBar.itemActiveIndex = 0
        }
    }



}


