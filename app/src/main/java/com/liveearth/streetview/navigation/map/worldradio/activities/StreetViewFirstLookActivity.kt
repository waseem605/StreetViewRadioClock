package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.LoadAdsStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.CategoryStreetViewCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewFirstLookBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.CategoryStreetViewAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.FamousStreetViewAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.CategoryStreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import javax.inject.Inject
import javax.inject.Named

class StreetViewFirstLookActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewFirstLookBinding
    private val TAG = "StreetViewFirstLook"
    private var mCategoryStreetViewList = ArrayList<CategoryStreetViewModel>()
    private var mFamousStreetViewList = ArrayList<CategoryStreetViewModel>()
    private lateinit var mAdapter:CategoryStreetViewAdapter
    private lateinit var mAdapterFamous: FamousStreetViewAdapter
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass


    @Inject
    @Named("FamousPlaces_StreetView_list")
    lateinit var mFamousPlacesStreetViewList :ArrayList<StreetViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewFirstLookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
        binding.toolbarLt.titleTx.text = "Street View"
        binding.toolbarLt.backLink.setOnClickListener {
            onBackPressed()
        }
        categoryRecyclerView()

        famousPlaces()
        initBannerAd()

    }

    private fun famousPlaces() {
        mFamousStreetViewList.add(CategoryStreetViewModel(R.drawable.maya_bay,"Maya Bay",0))
        mFamousStreetViewList.add(CategoryStreetViewModel(R.drawable.santa_onica,"South Beach",1))
        mFamousStreetViewList.add(CategoryStreetViewModel(R.drawable.burj_khalifa,"Burj Khalifa",2))
        mFamousStreetViewList.add(CategoryStreetViewModel(R.drawable.hagia_sopia,"Hagia Sopia",3))
        mFamousStreetViewList.add(CategoryStreetViewModel(R.drawable.space_needle,"Space Needle",4))
        mFamousStreetViewList.add(CategoryStreetViewModel(R.drawable.sydney_opera,"Opera House",5))
        mFamousStreetViewList.add(CategoryStreetViewModel(R.drawable.great_wall_china,"Great wall",6))
        mFamousStreetViewList.add(CategoryStreetViewModel(R.drawable.universal_studio,"Universal studio",7))
        mFamousStreetViewList.add(CategoryStreetViewModel(R.drawable.disney_land_paris,"Disneyland paris",8))

        mAdapterFamous = FamousStreetViewAdapter(mFamousStreetViewList,this,object :CategoryStreetViewCallBackListener{
            override fun onClickCategory(model: CategoryStreetViewModel, pos: Int) {
                val intentStreet = Intent(this@StreetViewFirstLookActivity,StreetViewMainActivity::class.java)
                intentStreet.putExtra(ConstantsStreetView.StreetView_ID,model.pos)
                intentStreet.putExtra(ConstantsStreetView.StreetView_Name,ConstantsStreetView.StreetView_Name)
                startActivity(intentStreet)
            }

        })
        val gridLayoutManager = GridLayoutManager(this,2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                return when (mAdapterFamous.getItemViewType(position)) {
                    0 -> gridLayoutManager.spanCount
                    1 -> 1
                    2 -> gridLayoutManager.spanCount
                    else -> -1
                }
            }
        }

        binding.famousRecycler.apply {
            setHasFixedSize(true)
            layoutManager =  gridLayoutManager
            adapter = mAdapterFamous
        }

    }

    private fun categoryRecyclerView() {
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.rivers,"River",0))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.mountains,"Mountain",1))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.theme_parks,"Theme\nParks",2))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.beaches,"Beaches",3))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.famous_roads,"Roads",4))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.iconic_buildings,"Iconic\nBuildings",5))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.hotels,"Hotels",6))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.dams,"Dams",7))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.presidential_palaces,"Presidential\nPlaces",8))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.ship_ports,"Ship\nPorts",9))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.world_wonders,"World\nwonder",10))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.football_stadiums,"Football\nstadium",11))

        mAdapter = CategoryStreetViewAdapter(mCategoryStreetViewList,this,object :CategoryStreetViewCallBackListener{
            override fun onClickCategory(model: CategoryStreetViewModel, pos: Int) {
                val intentStreet = Intent(this@StreetViewFirstLookActivity,StreetViewMainActivity::class.java)
                intentStreet.putExtra(ConstantsStreetView.StreetView_ID,model.pos)
                intentStreet.putExtra(ConstantsStreetView.StreetView_Name,"fh")
                startActivity(intentStreet)
            }

        })

        binding.categoryRecycler.apply {
            setHasFixedSize(true)
            layoutManager =  LinearLayoutManager(this@StreetViewFirstLookActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
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
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
//        binding.backFavourite.setBackgroundColor(Color.parseColor(backgroundSecondColor))
        binding.backOne.setColorFilter(Color.parseColor(backgroundColor))
//        binding.nearByLocations.setCardBackgroundColor(Color.parseColor(backgroundSecondColor))
    }
}