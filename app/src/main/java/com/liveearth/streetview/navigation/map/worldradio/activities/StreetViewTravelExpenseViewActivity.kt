package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExpenseCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewTravelExpenseViewBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.ExpenseSavedAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModelFactory

class StreetViewTravelExpenseViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewTravelExpenseViewBinding
    private lateinit var mExpenseViewModel:ExpenseViewModel
    private lateinit var mExpenseAdapter : ExpenseSavedAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewTravelExpenseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ExpenseViewModelFactory(this)
        mExpenseViewModel = ViewModelProvider(this,factory).get(ExpenseViewModel::class.java)
        mExpenseViewModel.getAllData().observe(this, Observer {
            it?.let {
                showExpenseList(it as ArrayList<ExpenseModel>)
            }
        })



        binding.toolbar.backLink.setOnClickListener {
            onBackPressed()
        }
        binding.toolbar.titleTx.text = "Expense List"
        binding.addExpenseBtn.setOnClickListener {
            val intent = Intent(this,StreetViewTravelExpenseActivity::class.java)
            intent.putExtra(ConstantsStreetView.EXPENSE_ID,-1)
            startActivity(intent)
        }
    }

    private fun showExpenseList(arrayList: ArrayList<ExpenseModel>) {
        mExpenseAdapter = ExpenseSavedAdapter(arrayList,this,object :ExpenseCallBackListener{
            override fun onExpenseView(model: ExpenseModel) {
                val intent = Intent(this@StreetViewTravelExpenseViewActivity,StreetViewTravelExpenseDetailsActivity::class.java)
                intent.putExtra(ConstantsStreetView.EXPENSE_ID,model.id)
                startActivity(intent)
            }

        })

        binding.addExpenseRecycler.apply {
            layoutManager = LinearLayoutManager(this@StreetViewTravelExpenseViewActivity)
            setHasFixedSize(true)
            adapter = mExpenseAdapter
        }


    }
}