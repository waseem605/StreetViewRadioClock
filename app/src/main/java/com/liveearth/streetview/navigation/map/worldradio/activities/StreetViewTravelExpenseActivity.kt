package com.liveearth.streetview.navigation.map.worldradio.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExpenseItemCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreeViewTravelExpenseBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.ExpenseItemAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationHelper
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class StreetViewTravelExpenseActivity : BaseStreetViewActivity() {
    private lateinit var binding: ActivityStreeViewTravelExpenseBinding
    private lateinit var mExpenseAdapter: ExpenseItemAdapter
    private var mExpenseList = ArrayList<ExpenseItemModel>()
    private lateinit var mExpenseViewModel: ExpenseViewModel
    private var datePickerDialog: DatePickerDialog? = null
    private var mMonth: Int? = null
    private var mYear: Int? = null
    private var mDay: Int? = null
    private var mDate: String? = null
    private var mTotal: Int = 0
    private var mID: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreeViewTravelExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            mID = intent.getIntExtra(ConstantsStreetView.EXPENSE_ID, 0)
            val factory = ExpenseViewModelFactory(this)
            mExpenseViewModel = ViewModelProvider(this, factory).get(ExpenseViewModel::class.java)

            if (mID >-1){
                showExpenseDetails(mID)
                "Update".also { binding.addExpense.text = it }
            }else if(mID == -1){
                binding.addExpense.text = "Add Expense"
            }
            else{
                binding.addExpense.text = "Add Expense"
              //  "Add Expense".also { binding.addExpense.text = it }
            }
            addExpenseItemRecycler()
            clickListenerExpense()

            binding.toolbar.backLink.setOnClickListener {
                onBackPressed()
            }
            binding.toolbar.titleTx.text = "Add Expense"

        } catch (e: Exception) {
        }

    }

    private fun clickListenerExpense() {
        binding.etLocations.text = ConstantsStreetView.CURRENT_ADDRESS

        binding.addNewItem.setOnClickListener {
            addItemNamePrice()
        }
        binding.addExpense.setOnClickListener {
            insertDataExpense()
        }

        binding.etCalender.setOnClickListener {
            datePickerListener()
        }


        binding.deleteExpenseBtn.setOnClickListener {
            if (mID != 0 ||mID ==null) {
                GlobalScope.launch(Dispatchers.IO) {
                    mExpenseViewModel.deleteDataById(mID)
                    withContext(Dispatchers.Main) {
                        setToast(this@StreetViewTravelExpenseActivity, "Delete Successfully")
                        onBackPressed()
                    }
                }
            } else {
                mExpenseList.clear()
                mExpenseAdapter.notifyDataSetChanged()
                binding.etTotalMoney.text = ""
                binding.etCategory.text.clear()
                binding.etLocations.text = ""
                binding.etCalender.text = ""
                binding.etDescription.text.clear()
            }
        }

        binding.shareExpenseCard.setOnClickListener {
            if (!validateCategory() || !validateDate() || !validateExpenseDescription() || mExpenseList.size == 0) {
                setToast(this, "please enter Fields")
            }else {
                LocationHelper.shareExpenseData(
                    this,
                    binding.etCategory.text.toString(),
                    binding.etCalender.text.toString(),
                    binding.etDescription.text.toString(),
                    binding.etLocations.text.toString(),
                    mExpenseList
                )
            }
        }

    }


    private fun showExpenseDetails(mID: Int) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                // mExpenseViewModel.getDataById(mID)
                showDataExpense(mExpenseViewModel.getDataById(mID))
            }
        } catch (e: Exception) {
        }
    }

    private fun showDataExpense(model: ExpenseModel?) {
        try {
            mExpenseList = model!!.itemList as ArrayList<ExpenseItemModel>
            binding.etTotalMoney.text = model.totalExpense.toString()
            binding.etCategory.setText(model.category)
            binding.etLocations.text = model.location
            binding.etCalender.text = model.date
            binding.etDescription.setText(model.description)
            addExpenseItemRecycler()

        } catch (e: Exception) {
        }



     /*   binding.shareExpenseCard.setOnClickListener {
            model!!.let {
                LocationHelper.shareExpenseData(this,it.category!!,it.date!!,it.description!!,it.location!!,it.itemList!! as ArrayList<ExpenseItemModel>)
            }
        }*/

    }

    private fun addExpenseItemRecycler() {
        try {
            mExpenseAdapter =
                ExpenseItemAdapter(mExpenseList, this, object : ExpenseItemCallBackListener {
                    override fun onExpenseAdd(model: ExpenseItemModel) {
                        mExpenseAdapter.notifyDataSetChanged()
                    }

                    override fun onRemoveItem(model: ExpenseItemModel, pos: Int) {
                        mExpenseList.removeAt(pos)
                        mExpenseAdapter.notifyDataSetChanged()
                    }

                })

            binding.addItemRecycler.apply {
                layoutManager = GridLayoutManager(this@StreetViewTravelExpenseActivity, 2)
                adapter = mExpenseAdapter
            }
        } catch (e: Exception) {
        }

    }

    private fun insertDataExpense() {

        if (!validateCategory() || !validateDate() || !validateExpenseDescription() || mExpenseList.size == 0) {
            setToast(this, "please enter Fields")
        } else {

            if (mID == null ||mID == 0) {
                mExpenseViewModel.insertExpense(
                    ExpenseModel(
                        id = null,
                        binding.etCategory.text.trim().toString(),
                        mDate,
                        ConstantsStreetView.CURRENT_ADDRESS,
                        mExpenseList,
                        binding.etDescription.text.toString(),
                        mTotal
                    )
                )
                setToast(this, "Saved Expense")
            } else {
                mExpenseViewModel.updateExpense(
                    ExpenseModel(
                        mID,
                        binding.etCategory.text.trim().toString(),
                        mDate,
                        ConstantsStreetView.CURRENT_ADDRESS,
                        mExpenseList,
                        binding.etDescription.text.toString(),
                        mTotal
                    )
                )
                setToast(this, "Updated Expense")
            }
            val intent = Intent(this,StreetViewTravelExpenseActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addItemNamePrice() {
        try {
            val nameItem = binding.etNameItem.text.trim().toString()
            val priceItem = binding.etItemPrice.text.trim().toString()
            if (!validateItemName() || !validateItemPrice()) {
                setToast(this@StreetViewTravelExpenseActivity, "Please fill item details")
            } else {
                mTotal += priceItem.toInt()
                binding.etTotalMoney.text = mTotal.toString()
                mExpenseList.add(ExpenseItemModel(nameItem, priceItem.toInt(), mExpenseList.size))
                mExpenseAdapter.notifyDataSetChanged()
                setToast(this@StreetViewTravelExpenseActivity, "added")
                binding.etItemPrice.text.clear()
                binding.etNameItem.text.clear()
            }
        } catch (e: Exception) {
        }

    }

    private fun datePickerListener() {
        val mCalendar = Calendar.getInstance()
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH)
        mMonth = mCalendar.get(Calendar.MONTH)
        mYear = mCalendar.get(Calendar.YEAR)
        datePickerDialog = DatePickerDialog(
            this, R.style.DialogTheme, { view, year, month, dayOfMonth ->
                binding.etCalender.text = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
                mDate = dayOfMonth.toString() + "/" + (month + 1) + "/" + year
            },
            mYear!!, mMonth!!, mDay!!
        )
        datePickerDialog!!.show()
    }

    private fun validateCategory(): Boolean {
        val temp: String
        temp = binding.etCategory.text.toString().trim { it <= ' ' }
        return if (temp.isEmpty()) {
            binding.etCategory.error = "Field can not be empty"
            false
        } else {
            binding.etCategory.error = null
            //binding.etCategory.setErrorEnabled(false)
            true
        }
    }

    private fun validateDate(): Boolean {
        val temp: String
        temp = binding.etCalender.text.toString().trim { it <= ' ' }
        return if (temp.isEmpty()) {
            binding.etCalender.error = "Field can not be empty"
            false
        } else {
            binding.etCalender.error = null
            //binding.etCategory.setErrorEnabled(false)
            true
        }
    }

    private fun validateItemName(): Boolean {
        val temp: String
        temp = binding.etNameItem.text.toString().trim { it <= ' ' }
        return if (temp.isEmpty()) {
            binding.etNameItem.error = "Field can not be empty"
            false
        } else {
            binding.etNameItem.error = null
            //binding.etCategory.setErrorEnabled(false)
            true
        }
    }

    private fun validateItemPrice(): Boolean {
        val temp: String
        temp = binding.etItemPrice.text.toString().trim { it <= ' ' }
        return if (temp.isEmpty()) {
            binding.etItemPrice.error = "Field can not be empty"
            false
        } else {
            binding.etItemPrice.error = null
            //binding.etCategory.setErrorEnabled(false)
            true
        }
    }

    private fun validateExpenseDescription(): Boolean {
        val `val`: String
        `val` = binding.etDescription.text.toString().trim { it <= ' ' }
        return if (`val`.isEmpty()) {
            binding.etDescription.error = "Field can not be empty"
            false
        } else {
            binding.etDescription.error = null
            // binding.etDescription.setErrorEnabled(false)
            true
        }
    }

}


