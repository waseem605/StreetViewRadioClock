package com.liveearth.streetview.navigation.map.worldradio.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreetViewExpenseTravelDetailsBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.ExpenseDetailsAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.R
import android.util.Log


class StreetViewTravelExpenseDetailsActivity : BaseStreetViewActivity() {
    private lateinit var binding: ActivityStreetViewExpenseTravelDetailsBinding
    private val TAG = "TravelDetails"
    private lateinit var mExpenseViewModel: ExpenseViewModel
    private lateinit var mAdapter: ExpenseDetailsAdapter

    private var mID:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreetViewExpenseTravelDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            mID  = intent.getIntExtra(ConstantsStreetView.EXPENSE_ID,0)
            clickListenerExpenseDetails(mID)
            showExpenseDetails(mID)
        } catch (e: Exception) {
        }

        binding.toolbar.titleTx.text = "Expense Details"
        binding.toolbar.backLink.setOnClickListener {
            onBackPressed()
        }

    }

    private fun showExpenseDetails(mID: Int) {
        try {
            val factory = ExpenseViewModelFactory(this)
            mExpenseViewModel = ViewModelProvider(this, factory).get(ExpenseViewModel::class.java)
            GlobalScope.launch(Dispatchers.IO) {
               // mExpenseViewModel.getDataById(mID)
                showDataExpense( mExpenseViewModel.getDataById(mID))
            }
        } catch (e: Exception) {
        }

    }

    private fun showDataExpense(model: ExpenseModel?) {
        try {
            binding.etTotalMoney.text = model!!.totalExpense.toString()
            binding.etCategory.text = model.category
            binding.etLocations.text = model.location
            binding.etCalender.text = model.date
            binding.etDescription.text = model.description

            mAdapter = ExpenseDetailsAdapter(model.itemList as ArrayList<ExpenseItemModel>,this)
            binding.showItemRecycler.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@StreetViewTravelExpenseDetailsActivity)
                adapter = mAdapter
            }
        } catch (e: Exception) {
        }

        binding.shareExpenseCard.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareTitle = model!!.category
            val shareDate = model.date
            val shareDesc = model.description
            val shareLocation = model.location
            var shareItem = ""
            for (i in 0 until model.itemList!!.size) {
               val shareItemTemp = model.itemList!![i].name+"    "+model.itemList!![i].Price
                shareItem +="\n$shareItemTemp"
            }
            val shareBody ="Category: $shareTitle \n Date: $shareDate \n Location: $shareLocation \n\n Items: $shareItem \n\n Description: $shareDesc"
            Log.d(TAG, "showDataExpense: $shareBody")
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "getString(R.string.share_subject)")
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
           startActivity(Intent.createChooser(intent, "getString(R.string.share_using)"))
        }

    }

    private fun clickListenerExpenseDetails(mID: Int) {

        binding.toolbar.backLink.setOnClickListener {
            onBackPressed()
        }

        binding.editExpenseBtn.setOnClickListener {
            val intent = Intent(this,StreetViewTravelExpenseActivity::class.java)
            intent.putExtra(ConstantsStreetView.EXPENSE_ID, mID)
            startActivity(intent)
        }

        binding.deleteExpenseBtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                mExpenseViewModel.deleteDataById(mID)
                withContext(Dispatchers.Main) {
                    setToast(this@StreetViewTravelExpenseDetailsActivity, "Delete Successfully")
                    onBackPressed()
                }
            }
        }
    }
}