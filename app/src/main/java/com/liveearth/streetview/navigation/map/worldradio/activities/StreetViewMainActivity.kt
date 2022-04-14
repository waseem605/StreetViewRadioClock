package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.CategoryStreetViewCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MainStreetViewCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewMainBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.StreetViewMainAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.CategoryStreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class StreetViewMainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewMainBinding
    private val TAG = "StreetViewMain"
    private lateinit var mAdapter:StreetViewMainAdapter
    private var mGeneralStreetViewList =ArrayList<StreetViewModel>()
    private var mLatitude:Double = 0.0
    private var mLongitude:Double = 0.0
    private lateinit var mLocationRepository: LocationRepository


    @Inject
    @Named("River_StreetView_list")
    lateinit var mRiverStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("Mountain_StreetView_list")
    lateinit var mMountainStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("ThemeParks_StreetView_list")
    lateinit var mThemeParksStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("Beaches_StreetView_list")
    lateinit var mBeachesStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("Airports_StreetView_list")
    lateinit var mAirportsStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("Dams_StreetView_list")
    lateinit var mDamsStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("Football_StreetView_list")
    lateinit var mFootballStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("Hotels_StreetView_list")
    lateinit var mHotelsStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("Roads_StreetView_list")
    lateinit var mRoadsStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("IconicBuilding_StreetView_list")
    lateinit var mIconicBuildingsStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("Presidential_StreetView_list")
    lateinit var mPresidentialStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("WorldWonder_StreetView_list")
    lateinit var mWorldWondersStreetViewList :ArrayList<StreetViewModel>

    @Inject
    @Named("ShipPorts_StreetView_list")
    lateinit var mShipPortsStreetViewList :ArrayList<StreetViewModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            val posIntent = intent.getIntExtra(ConstantsStreetView.StreetView_ID,0)
            Log.d(TAG, "onCreate: =======mStreetViewListTwo========="+mRiverStreetViewList.size)

            getStreetViewData(posIntent)

            getCurrentUserLocation()

        } catch (e: Exception) {
        }

    }

    private fun getCurrentUserLocation() {
        mLocationRepository = LocationRepository(this,object :MyLocationListener{
            override fun onLocationChanged(location: Location) {
                location.let {
                    mLatitude = it.latitude
                    mLongitude = it.longitude
                }
            }

        })
    }


    private fun getStreetViewData(posIntent: Int) {

        when(posIntent){
            0->{
                mGeneralStreetViewList = mRiverStreetViewList
            }
            1->{
                mGeneralStreetViewList = mMountainStreetViewList
            }
            2->{
                mGeneralStreetViewList = mThemeParksStreetViewList
            }
            3->{
                mGeneralStreetViewList = mBeachesStreetViewList
            }
            4->{
                mGeneralStreetViewList = mRoadsStreetViewList
            }
            5->{
                mGeneralStreetViewList = mIconicBuildingsStreetViewList
            }
            6->{
                mGeneralStreetViewList = mHotelsStreetViewList
            }
            7->{
                mGeneralStreetViewList = mDamsStreetViewList
            }
            8->{
                mGeneralStreetViewList =mPresidentialStreetViewList
            }
            9->{
                mGeneralStreetViewList =mShipPortsStreetViewList
            }
            10->{
                mGeneralStreetViewList =mWorldWondersStreetViewList
            }
            11->{
                mGeneralStreetViewList =mFootballStreetViewList
            }
        }

        mAdapter = StreetViewMainAdapter(mGeneralStreetViewList,this,object :
            MainStreetViewCallBackListener {
            override fun onClickShareCategory(model: StreetViewModel) {
                LocationHelper.shareLocation(this@StreetViewMainActivity,model.lat.toDouble(),model.long.toDouble())
            }

            override fun onClickNavigateCategory(model: StreetViewModel) {
                val intent = Intent(this@StreetViewMainActivity, StreetViewRouteActivity::class.java)
                intent.putExtra(ConstantsStreetView.OriginLatitude, mLatitude)
                intent.putExtra(ConstantsStreetView.OriginLongitude, mLongitude)
                intent.putExtra(ConstantsStreetView.MultiPointsRoute,false)
                intent.putExtra(ConstantsStreetView.DestinationLatitude, model.lat.toDouble())
                intent.putExtra(ConstantsStreetView.DestinationLongitude, model.long.toDouble())
                startActivity(intent)
            }

        })

        binding.streetViewViewPager.adapter = mAdapter
        binding.streetViewViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL




    }
}