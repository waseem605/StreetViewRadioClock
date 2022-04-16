package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.CategoryStreetViewCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewFirstLookBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.CategoryStreetViewAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.FamousStreetViewAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.CategoryStreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.StreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import javax.inject.Inject
import javax.inject.Named

class StreetViewFirstLookActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewFirstLookBinding
    private val TAG = "StreetViewFirstLook"
    private var mCategoryStreetViewList = ArrayList<CategoryStreetViewModel>()
    private var mFamousStreetViewList = ArrayList<CategoryStreetViewModel>()
    private lateinit var mAdapter:CategoryStreetViewAdapter
    private lateinit var mAdapterFamous: FamousStreetViewAdapter


    @Inject
    @Named("FamousPlaces_StreetView_list")
    lateinit var mFamousPlacesStreetViewList :ArrayList<StreetViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewFirstLookBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.toolbarLt.titleTx.text = "Street View"
        binding.toolbarLt.backLink.setOnClickListener {
            onBackPressed()
        }
        categoryRecyclerView()

        famousPlaces()

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

        binding.famousRecycler.apply {
            setHasFixedSize(true)
            layoutManager =  GridLayoutManager(this@StreetViewFirstLookActivity,2)
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
}