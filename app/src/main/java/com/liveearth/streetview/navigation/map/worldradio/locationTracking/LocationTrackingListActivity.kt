package com.liveearth.streetview.navigation.map.worldradio.locationTracking

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.liveearth.streetview.navigation.map.worldradio.activities.MainActivity
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityLocationTrackingListBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.TrackLocationItemAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.TrackLocationItemBottomAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Tracking_roomDb.TrackLocationViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocationTrackingListActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLocationTrackingListBinding
    private val TAG = "LocationTrackingList"
    private lateinit var mTrackLocationViewModel: TrackLocationViewModel
    private var mBottomSheetBehavior: BottomSheetBehavior<View>?=null
    private var mLocationTrackedList = ArrayList<TrackLocationModel>()
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass

    private lateinit var mAdapter :TrackLocationItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationTrackingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
        onClickListener()
        getItemTrackedLocationItem()
    }

    private fun getItemTrackedLocationItem() {
        try {
            val trackFactory = TrackLocationViewModelFactory(this)
            mTrackLocationViewModel =
                ViewModelProvider(this, trackFactory).get(TrackLocationViewModel::class.java)

            mTrackLocationViewModel.getAllData().observe(this, Observer {
                it?.let {
                    mLocationTrackedList = it as ArrayList<TrackLocationModel>
                    showItemTrackedLocationItem(it as ArrayList<TrackLocationModel>)
                }
            })

        } catch (e: Exception) {
        }
    }

    private fun showItemTrackedLocationItem(arrayList: ArrayList<TrackLocationModel>) {

         mAdapter = TrackLocationItemAdapter(arrayList,this,object :TrackingLocationCallBackClick{
            override fun onClickItemLocation(model: TrackLocationModel, pos: Int) {
                val intentRoute = Intent(this@LocationTrackingListActivity,TrackedRouteLocationsActivity::class.java)
                intentRoute.putExtra(ConstantsStreetView.dataIdSaved,model.id)
                startActivity(intentRoute)
            //getSelectedDateData(model.date)
            }

            override fun onClickDeleteLocation(model: TrackLocationModel,pos: Int) {

                try {
                    GlobalScope.launch {
                        Dispatchers.IO
                        mTrackLocationViewModel.deleteTrackingById(model.id!!)
                    }
                    mAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                }
            }
        })

        binding.locationListTracking.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@LocationTrackingListActivity)
            adapter = mAdapter
        }
    }
/*

    private fun getSelectedDateData(date: String?) {
        if(mBottomSheetBehavior!!.state==BottomSheetBehavior.STATE_EXPANDED){
            mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED
        }else {
            mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
        val mLocationList = ArrayList<TrackLocationModel>()
        for (i in 0 until mLocationTrackedList.size){
            if (date ==mLocationTrackedList[i].date ) {
                mLocationList.add(mLocationTrackedList[i])
            }
        }

        val mAdapterBottom = TrackLocationItemBottomAdapter(mLocationList,this,object :TrackingLocationCallBackClick{
            override fun onClickItemLocation(model: TrackLocationModel, pos: Int) {

                val intentRoute = Intent(this@LocationTrackingListActivity,TrackedRouteLocationsActivity::class.java)
                intentRoute.putExtra(ConstantsStreetView.dataIdSaved,model.id)
                startActivity(intentRoute)
            }

        })
        binding.bottomSheetLocation.savedTimeRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@LocationTrackingListActivity)
            adapter = mAdapterBottom
        }
    }
*/

    private fun onClickListener() {
        binding.toolbarLt.titleTx.text = "Tracked Locations"

        binding.toolbarLt.backLink.setOnClickListener {
            onBackPressed()
        }

/*        mBottomSheetBehavior=BottomSheetBehavior.from(binding.bottomSheetLocation.bottomSheetDrawer)
        mBottomSheetBehavior!!.state=BottomSheetBehavior.STATE_COLLAPSED*/
    }

    override fun onBackPressed() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
    }
}