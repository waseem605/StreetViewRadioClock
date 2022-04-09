package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.FavLocationListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewFavouriteLocationsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.FavouriteLocationsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Favourite_roomDb.FavouriteLocationViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StreetViewFavouriteLocationsActivity : BaseStreetViewActivity() {
    private lateinit var binding:ActivityStreetViewFavouriteLocationsBinding
    private lateinit var mFavouriteLocationViewModel: FavouriteLocationViewModel
    private lateinit var mLocationRepository: LocationRepository
    private lateinit var mFavouriteAdapter: FavouriteLocationsAdapter
    private var mLatitude :Double = 0.0
    private var mLongitude :Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewFavouriteLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        getCurrentLocation()

    }

    private fun getCurrentLocation() {
        mLocationRepository = LocationRepository(this,object :MyLocationListener{
            override fun onLocationChanged(location: Location) {
                location.run {
                    mLatitude = location.latitude
                    mLongitude = location.longitude
                    mLocationRepository.stopLocation()
                }?: Runnable {
                    mLocationRepository.startLocation()
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
                LocationHelper.shareLocation(this@StreetViewFavouriteLocationsActivity,model.latitude!!,model.longitude!!)
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
}