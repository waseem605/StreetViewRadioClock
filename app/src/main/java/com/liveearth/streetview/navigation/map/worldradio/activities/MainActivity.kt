package com.liveearth.streetview.navigation.map.worldradio.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.centurionnavigation.dialogs.ExitDialogueBoxStreetView
import com.example.centurionnavigation.dialogs.LocationRequestDialogueBox
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.AppPurchaseHelperStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.LoadAdsStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ColorThemeCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExistCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityMainBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ColorModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationStreetViewHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), ColorThemeCallBackListener
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
        setThemeColor()

        initBannerAd()
        ConstantsStreetView.accessToken = mPreferenceManagerClass.getString(ConstantsStreetView.KEY_MAPBOX, ConstantsStreetView.accessToken)!!
        Log.d(TAG, "onCreate: mapbox id"+ ConstantsStreetView.accessToken)
        CoroutineScope(Dispatchers.IO).launch() {
            LocationStreetViewHelper.providerStreetViewEnabled(this@MainActivity)
            val isPermissionDone = LocationStreetViewHelper.locationStreetViewProvided(this@MainActivity)

            if (isPermissionDone) {
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "onCreateView: =====ispermissionDone=="+isPermissionDone)
                }
            }
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        //checkLocationPermission()
       // setupSmoothBottomMenu()

        bottomBarClickListener()

       /* navController.addOnDestinationChangedListener { _, destination, _ ->
            val billingHelper = AppPurchaseHelperStreetViewClock(this)
            if (!billingHelper.shouldShowAds()){

            }else{

            }
        }*/

    }

    private fun bottomBarClickListener() {
        val billingHelper = AppPurchaseHelperStreetViewClock(this)
        if (billingHelper.shouldShowAds()){
            setupSmoothBottomMenu()
            binding.bottomBar.setOnItemSelectedListener {
                pos = it
                when(it){
                    0->{
                        navController.navigate(R.id.homeFragment)
                        //binding.bottomBar.itemActiveIndex = 0
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

        }else{
            binding.bottomBarPremium.visibility = View.VISIBLE
            binding.bottomBar.visibility = View.GONE
            setupSmoothBottomMenuPremium()

            binding.bottomBarPremium.setOnItemSelectedListener {
                pos = it
                when(it){
                    0->{
                        navController.navigate(R.id.homeFragment)
                    }
                    1->{
                        navController.navigate(R.id.themeFragment)
                    }
                    2->{
                        navController.navigate(R.id.settingFragment)
                    }
                }
            }
        }
    }

    private fun setupSmoothBottomMenu() {
        val popupMenu = PopupMenu(this, null)

//        val billingHelper = AppPurchaseHelperStreetViewClock(this)
//        if (!billingHelper.shouldShowAds()){
//            popupMenu.inflate(R.menu.street_bottom_menu_premium)
//            Log.d("setupSmoothBottomMenu", "setupSmoothBottomMenu: ******hide premium******")
//        }else{
//            popupMenu.inflate(R.menu.street_bottom_menu)
//            Log.d("setupSmoothBottomMenu", "setupSmoothBottomMenu: ******show premium******")
//        }
        popupMenu.inflate(R.menu.street_bottom_menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }
    private fun setupSmoothBottomMenuPremium() {
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.street_bottom_menu_premium)
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
        val backgroundColorSecond = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second,"#237157")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        ConstantsStreetView.APP_SELECTED_COLOR = backgroundColor
        ConstantsStreetView.APP_SELECTED_SECOND_COLOR = backgroundColorSecond
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.bottomImage.setColorFilter( Color.parseColor(backgroundColor))
    }

    override fun onResume() {
        super.onResume()
        setThemeColor()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //mainItemClickListener()
            }
        } else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", this.packageName, null),
                    ),
                )
            }
        }
    }

      fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                val permissionDialog = LocationRequestDialogueBox(this,object :ExistCallBackListener{
                    override fun onExistClick() {
                        requestLocationPermission()
                    }
                })
                permissionDialog.show()

            } else {
                requestLocationPermission()
            }
        }else{
            //mainItemClickListener()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }

    private fun initBannerAd() {
        LoadAdsStreetViewClock.loadEarthLiveMapBannerAdMob(
            binding.bannerAd.adContainer,
            binding.bannerID,
            this
        )
    }

}


