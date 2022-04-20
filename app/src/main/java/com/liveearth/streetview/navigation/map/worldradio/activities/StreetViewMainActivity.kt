package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MainStreetViewCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewMainBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.StreetViewMainAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepository
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import dagger.hilt.android.AndroidEntryPoint
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


    @Inject
    @Named("FamousPlaces_StreetView_list")
    lateinit var mFamousPlacesStreetViewList :ArrayList<StreetViewModel>
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
         mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
        try {
            binding.toolbarLt.backLink.setOnClickListener {
                onBackPressed()
            }
            binding.toolbarLt.titleTx.text = "Street View"
            val posIntent = intent.getIntExtra(ConstantsStreetView.StreetView_ID,0)
            val nameIntent = intent.getStringExtra(ConstantsStreetView.StreetView_Name)
            if (nameIntent == ConstantsStreetView.StreetView_Name){
                mGeneralStreetViewList = mFamousPlacesStreetViewList
                famousStreetViewDisplay(mGeneralStreetViewList,posIntent)
                Log.d(TAG, "onCreate: "+mGeneralStreetViewList.size)
                for (i in 0 until mGeneralStreetViewList.size){
                    Log.d(TAG, "onCreate:===========$i===== "+mGeneralStreetViewList[i].name)
                }

                binding.famousLayoutStreetView.visibility = View.VISIBLE

            }else
            {
                Log.d(TAG, "onCreate: =======mStreetViewListTwo=========" + mRiverStreetViewList.size)

                getStreetViewData(posIntent)
                getCurrentUserLocation()
            }

        } catch (e: Exception) {
        }

    }

    private fun famousStreetViewDisplay(mGeneralStreetViewList: ArrayList<StreetViewModel>, posIntent: Int) {
        binding.streetViewNameItem.text = mGeneralStreetViewList[posIntent].name
        binding.streetViewDescriptionItem.text = mGeneralStreetViewList[posIntent].description
        Glide.with(this).load(mGeneralStreetViewList[posIntent].link).diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.INVISIBLE
                   // binding.streetViewImageItem.setImageDrawable(resource)
                    return true
                }

            }).into(binding.streetViewImageItem)

        binding.shareLocationCard.setOnClickListener {
            LocationHelper.shareLocation(this@StreetViewMainActivity,mGeneralStreetViewList[posIntent].lat.toDouble(),mGeneralStreetViewList[posIntent].long.toDouble())
        }

        binding.navigateCard.setOnClickListener {
            val intent = Intent(this@StreetViewMainActivity, StreetViewRouteActivity::class.java)
            intent.putExtra(ConstantsStreetView.OriginLatitude, mLatitude)
            intent.putExtra(ConstantsStreetView.OriginLongitude, mLongitude)
            intent.putExtra(ConstantsStreetView.MultiPointsRoute,false)
            intent.putExtra(ConstantsStreetView.DestinationLatitude, mGeneralStreetViewList[posIntent].lat.toDouble())
            intent.putExtra(ConstantsStreetView.DestinationLongitude, mGeneralStreetViewList[posIntent].long.toDouble())
            startActivity(intent)
        }

        binding.onLiveEarthCard.setOnClickListener {
            navigateToLiveEarth(mGeneralStreetViewList[posIntent].lat.toDouble(), mGeneralStreetViewList[posIntent].long.toDouble())
        }



    }

    private fun getCurrentUserLocation() {
        mLocationRepository = LocationRepository(this,object :MyLocationListener{
            override fun onLocationChanged(location: Location) {
                location.let {
                    mLatitude = it.latitude
                    mLongitude = it.longitude
                    mLocationRepository.stopLocation()
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

            override fun onClickNavigateLiveEarth(model: StreetViewModel) {
                navigateToLiveEarth(model.lat.toDouble(),model.long.toDouble())
            }

        })

        binding.streetViewViewPager.adapter = mAdapter
        binding.streetViewViewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

    }

    private fun navigateToLiveEarth(lat:Double, long:Double){
        val intent = Intent(this@StreetViewMainActivity, StreetViewLiveEarthActivity::class.java)
        intent.putExtra(ConstantsStreetView.OriginLatitude, lat)
        intent.putExtra(ConstantsStreetView.OriginLongitude, long)
        startActivity(intent)
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
        binding.navigateCard.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.shareLocationCard.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.onLiveEarthCard.setCardBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))

    }
}