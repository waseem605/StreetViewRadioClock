package com.liveearth.streetview.navigation.map.worldradio.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.FavLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewFavouriteLocationsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.FavouriteLocationsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.FavouriteLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.FavouriteLocationViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.FavouriteLocationViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StreetViewFavouriteLocationsActivity : BaseStreetViewActivity() {
    private lateinit var binding:ActivityStreetViewFavouriteLocationsBinding
    private lateinit var mFavouriteLocationViewModel: FavouriteLocationViewModel

    private lateinit var mFavouriteAdapter: FavouriteLocationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewFavouriteLocationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = FavouriteLocationViewModelFactory(this)
        mFavouriteLocationViewModel = ViewModelProvider(this,factory).get(FavouriteLocationViewModel::class.java)

        mFavouriteLocationViewModel.getAllData().observe(this, Observer {
            it.let {
                binding.emptyListLt.visibility = View.GONE
                showRecyclerItemLocations(it as ArrayList)
            }
        })


    }

    private fun showRecyclerItemLocations(arrayList: ArrayList<FavouriteLocationModel>) {

        mFavouriteAdapter = FavouriteLocationsAdapter(arrayList,this,object :FavLocationListener{
            override fun onNavigateToLocation(model: FavouriteLocationModel) {

            }

            override fun onShareFavLocation(model: FavouriteLocationModel) {
                LocationHelper.shareLocation(this@StreetViewFavouriteLocationsActivity,model.latitude!!,model.longitude!!)
            }

            override fun onDeleteFavLocation(model: FavouriteLocationModel) {
                GlobalScope.launch(Dispatchers.Main) {
                    mFavouriteLocationViewModel.deleteFavouriteLocation(model)
                    setToast(this@StreetViewFavouriteLocationsActivity,"Delete item successfully")
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