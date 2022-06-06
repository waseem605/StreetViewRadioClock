package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.LoadAdsStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.FavLocationListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewFavouriteLocationsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.FavouriteLocationsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelperAssistant
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepositoryStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StreetViewFavouriteLocationsActivity : BaseStreetViewActivity() {
    private lateinit var binding:ActivityStreetViewFavouriteLocationsBinding
    private lateinit var mFavouriteLocationViewModel: FavouriteLocationViewModel
    private lateinit var mLocationRepositoryStreetView: LocationRepositoryStreetView
    private lateinit var mFavouriteAdapter: FavouriteLocationsAdapter
    private lateinit var mPreferenceManagerClass:PreferenceManagerClass
    private var mLatitude :Double = 0.0
    private var mLongitude :Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewFavouriteLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
        showProgressDialog(this)
        val factory = FavouriteLocationViewModelFactory(this)
        mFavouriteLocationViewModel = ViewModelProvider(this,factory).get(FavouriteLocationViewModel::class.java)

        mFavouriteLocationViewModel.getAllData().observe(this, Observer {
            it.let {
                binding.emptyListLt.visibility = View.GONE
                showRecyclerItemLocations(it as ArrayList)
                hideProgressDialog(this)
            }
        })

        binding.toolbar.backLink.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.titleTx.text = "Favourite Locations"

        binding.nearByLocations.setOnClickListener {
            val intent = Intent(this,StreetViewNearByPlacesActivity::class.java)
            startActivity(intent)
        }
        getCurrentLocation()
        initBannerAd()
    }

    private fun getCurrentLocation() {
        mLocationRepositoryStreetView = LocationRepositoryStreetView(this,object :MyLocationListener{
            override fun onLocationChanged(location: Location) {
                location.run {
                    mLatitude = location.latitude
                    mLongitude = location.longitude
                    mLocationRepositoryStreetView.stopLocation()
                }?: Runnable {
                    mLocationRepositoryStreetView.startLocation()
                }
            }

        })
    }

    private fun showRecyclerItemLocations(arrayList: ArrayList<FavouriteLocationModel>) {

        mFavouriteAdapter = FavouriteLocationsAdapter(arrayList,this,object :FavLocationListener{
            override fun onNavigateToLocation(model: FavouriteLocationModel) {
                if (mLongitude !=0.0 && model.latitude !=0.0) {
                    val intent = Intent(
                        this@StreetViewFavouriteLocationsActivity,
                        StreetViewRouteActivity::class.java
                    )
                    intent.putExtra(ConstantsStreetView.OriginLatitude, mLatitude)
                    intent.putExtra(ConstantsStreetView.OriginLongitude, mLongitude)
                    intent.putExtra(ConstantsStreetView.DestinationLatitude, model.latitude)
                    intent.putExtra(ConstantsStreetView.DestinationLongitude, model.longitude)
                    startActivity(intent)
                }else{
                    setToast(this@StreetViewFavouriteLocationsActivity,"Your Location not found")
                }
            }

            override fun onShareFavLocation(model: FavouriteLocationModel) {
                LocationHelperAssistant.shareLocation(this@StreetViewFavouriteLocationsActivity,model.latitude!!,model.longitude!!)
            }

            override fun onDeleteFavLocation(model: FavouriteLocationModel) {
                try {
                    GlobalScope.launch(Dispatchers.Main) {
                        mFavouriteLocationViewModel.deleteFavouriteLocation(model)
                        setToast(this@StreetViewFavouriteLocationsActivity,"Delete item successfully")
                    }
                } catch (e: Exception) {
                }
            }

        })
        binding.savedLocationRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@StreetViewFavouriteLocationsActivity)
            adapter = mFavouriteAdapter
        }

    }

    private fun initBannerAd() {
        LoadAdsStreetViewClock.loadEarthLiveMapBannerAdMob(
            binding.bannerAd.adContainer,
            binding.bannerID,
            this
        )
    }


    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.toolbar.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.backFavourite.setBackgroundColor(Color.parseColor(backgroundColor))
       // binding.backOne.setColorFilter(Color.parseColor(backgroundSecondColor))
        binding.nearByLocations.setBackgroundColor(Color.parseColor(backgroundColor))
    }
}