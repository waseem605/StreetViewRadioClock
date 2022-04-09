package com.liveearth.streetview.navigation.map.worldradio.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExpenseCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreeViewTravelExpenseBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewAdapter.ExpenseItemAdapter
import com.liveearth.streetview.navigation.map.worldradio.streetViewModel.ExpenseItemModel
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModel
import com.liveearth.streetview.navigation.map.worldradio.streetView_roomDb.Expense_roomDb.ExpenseViewModelFactory
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
    private var mTotal:Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreeViewTravelExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ExpenseViewModelFactory(this)
        mExpenseViewModel = ViewModelProvider(this, factory).get(ExpenseViewModel::class.java)

        addExpenseItemRecycler()
        clickListenerExpense()

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

    }

    private fun addExpenseItemRecycler() {
        mExpenseAdapter = ExpenseItemAdapter(mExpenseList, this, object : ExpenseCallBackListener {
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

    }

    private fun insertDataExpense() {

        if (!validateCategory() || !validateDate() || !validateExpenseDescription() || mExpenseList.size ==0) {
            setToast(this, "please enter Fields")
        } else {
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
        }

    }

    private fun addItemNamePrice() {
        try {
            val nameItem = binding.etNameItem.text.trim().toString()
            val priceItem = binding.etItemPrice.text.trim().toString()
            if (!validateItemName() || !validateItemPrice()){
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
        `val` = binding.etDescription.getText().toString().trim { it <= ' ' }
        return if (`val`.isEmpty()) {
            binding.etDescription.setError("Field can not be empty")
            false
        } else {
            binding.etDescription.setError(null)
           // binding.etDescription.setErrorEnabled(false)
            true
        }
    }

}


