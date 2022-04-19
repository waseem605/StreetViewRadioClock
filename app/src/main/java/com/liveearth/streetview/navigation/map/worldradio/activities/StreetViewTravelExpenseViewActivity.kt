package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExpenseCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewTravelExpenseViewBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.ExpenseSavedAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModelFactory

class StreetViewTravelExpenseViewActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStreetViewTravelExpenseViewBinding
    private lateinit var mExpenseViewModel:ExpenseViewModel
    private lateinit var mExpenseAdapter : ExpenseSavedAdapter
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewTravelExpenseViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
            mPreferenceManagerClass = PreferenceManagerClass(this)
        setThemeColor()
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


    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor(backgroundColor)
        binding.expenseViewBack.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.addExpenseBtn.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.toolbar.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))

    }

    override fun onBackPressed() {
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}