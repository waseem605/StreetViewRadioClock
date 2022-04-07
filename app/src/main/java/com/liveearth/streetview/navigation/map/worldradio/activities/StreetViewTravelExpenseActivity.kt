package com.liveearth.streetview.navigation.map.worldradio.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExpenseCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreeViewTravelExpenseBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.ExpenseItemAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel

class StreetViewTravelExpenseActivity : BaseStreetViewActivity() {
    private lateinit var binding: ActivityStreeViewTravelExpenseBinding
    private lateinit var mExpenseAdapter: ExpenseItemAdapter
    private var  mExpenseList = ArrayList<ExpenseItemModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreeViewTravelExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        addExpenseItemRecycler()

    }

    private fun addExpenseItemRecycler() {

        //mExpenseList.add(ExpenseItemModel("",0,0))
        binding.addNewItem.setOnClickListener {
            mExpenseList.add(ExpenseItemModel("name",0,0))
            mExpenseAdapter.notifyDataSetChanged()
        }
        mExpenseAdapter = ExpenseItemAdapter(mExpenseList,this,object :ExpenseCallBackListener{
            override fun onExpenseAdd(model: ExpenseItemModel) {

                setToast(this@StreetViewTravelExpenseActivity,"added")
                mExpenseAdapter.notifyDataSetChanged()

            }

            override fun onRemoveItem(model: ExpenseItemModel, pos: Int) {

                mExpenseAdapter.notifyDataSetChanged()

            }

        })

        binding.addItemRecycler.apply {
            layoutManager = LinearLayoutManager(this@StreetViewTravelExpenseActivity)
            adapter = mExpenseAdapter
        }

    }


}