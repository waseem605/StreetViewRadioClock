package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewTravelExpenseViewBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.ExpenseSavedAdapter
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
            it.let {
                showExpenseList(it as ArrayList<ExpenseModel>)
            }
        })


        binding.addExpenseBtn.setOnClickListener {
            val intent = Intent(this,StreetViewTravelExpenseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showExpenseList(arrayList: ArrayList<ExpenseModel>) {
        mExpenseAdapter = ExpenseSavedAdapter(arrayList,this,)

        binding.addExpenseRecycler.apply {
            layoutManager = LinearLayoutManager(this@StreetViewTravelExpenseViewActivity)
            setHasFixedSize(true)
            adapter = mExpenseAdapter
        }


    }
}