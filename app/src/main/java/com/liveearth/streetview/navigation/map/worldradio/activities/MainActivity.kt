package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.centurionnavigation.dialogs.ExitDialogueBoxStreetView
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ColorThemeCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExistCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityMainBinding
import com.liveearth.streetview.navigation.map.worldradio.locationTracking.LocationTrackingMainActivity
import com.liveearth.streetview.navigation.map.worldradio.streetViewFragments.ThemeFragment
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ColorModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import java.io.File


class MainActivity : BaseStreetViewActivity(), ColorThemeCallBackListener
{
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    private lateinit var navController: NavController
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass
    var pos = 0
    var count = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mPreferenceManagerClass = PreferenceManagerClass(this)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        checkLocationPermission()
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

    override fun onColorClick(colorModel: ColorModel) {
        Log.d(TAG, "onColorClick: ===========main++++++++++++++")
        setThemeColor()
    }



    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR,"#237157")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.bottomImage.setColorFilter( Color.parseColor(backgroundColor) )
    }



}


