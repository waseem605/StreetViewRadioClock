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
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.CategoryStreetViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView

class StreetViewFirstLookActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewFirstLookBinding
    private val TAG = ""
    private var mCategoryStreetViewList = ArrayList<CategoryStreetViewModel>()
    private lateinit var mAdapter:CategoryStreetViewAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewFirstLookBinding.inflate(layoutInflater)
        setContentView(binding.root)


        categoryRecyclerView()


    }

    private fun categoryRecyclerView() {
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.ic_weather_temp,"River",0))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.football_stadiums,"Mountain",1))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.ic_weather_temp,"Parks",2))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.ic_weather_temp,"River",3))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.ic_weather_temp,"River",4))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.ic_weather_temp,"River",5))
        mCategoryStreetViewList.add(CategoryStreetViewModel(R.drawable.ic_weather_temp,"River",6))

        mAdapter = CategoryStreetViewAdapter(mCategoryStreetViewList,this,object :CategoryStreetViewCallBackListener{
            override fun onClickCategory(model: CategoryStreetViewModel, pos: Int) {
                val intentStreet = Intent(this@StreetViewFirstLookActivity,StreetViewMainActivity::class.java)
                intentStreet.putExtra(ConstantsStreetView.StreetView_ID,model.pos)
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