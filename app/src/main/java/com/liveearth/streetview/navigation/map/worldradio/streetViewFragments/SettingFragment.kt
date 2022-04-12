package com.liveearth.streetview.navigation.map.worldradio.streetViewFragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.FragmentSettingBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass

class SettingFragment : Fragment() {
    private lateinit var binding:FragmentSettingBinding
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater,container,false)
        mPreferenceManagerClass = PreferenceManagerClass(requireContext())

        clickListenerSetting()

        return binding.root
    }

    private fun clickListenerSetting() {
        binding.toolbarLt.titleTx.text =getString(R.string.setting)

        //Distance Unit
        if (mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Miles,false)){
            binding.milesTx.typeface = Typeface.DEFAULT_BOLD
            binding.milesTx.setTextColor(resources.getColor(R.color.appMainColor))
            binding.switchUnit.isChecked = true
        }else{
            binding.meterTx.typeface = Typeface.DEFAULT_BOLD
            binding.meterTx.setTextColor(resources.getColor(R.color.appMainColor))
            binding.switchUnit.isChecked = false
        }

        //Distance Unit switch
        binding.switchUnit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.milesTx.typeface = Typeface.DEFAULT_BOLD
                binding.milesTx.setTextColor(resources.getColor(R.color.appMainColor))
                binding.meterTx.typeface = Typeface.DEFAULT
                binding.meterTx.setTextColor(resources.getColor(R.color.black))
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Miles,true)
            } else {
                binding.meterTx.typeface = Typeface.DEFAULT_BOLD
                binding.meterTx.setTextColor(resources.getColor(R.color.appMainColor))
                binding.milesTx.typeface = Typeface.DEFAULT
                binding.milesTx.setTextColor(resources.getColor(R.color.black))
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Miles,false)
            }
        }

        //Temperature Unit
        if (mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)){
            binding.fahrenheitTx.typeface = Typeface.DEFAULT_BOLD
            binding.fahrenheitTx.setTextColor(resources.getColor(R.color.appMainColor))
            binding.switchUnitTemp.isChecked = true
        }else{
            binding.celsiusTx.typeface = Typeface.DEFAULT_BOLD
            binding.celsiusTx.setTextColor(resources.getColor(R.color.appMainColor))
            binding.switchUnitTemp.isChecked = false
        }

        //Temperature Unit switch
        binding.switchUnitTemp.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.fahrenheitTx.typeface = Typeface.DEFAULT_BOLD
                binding.fahrenheitTx.setTextColor(resources.getColor(R.color.appMainColor))
                binding.celsiusTx.typeface = Typeface.DEFAULT
                binding.celsiusTx.setTextColor(resources.getColor(R.color.black))
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,true)
            } else {
                binding.celsiusTx.typeface = Typeface.DEFAULT_BOLD
                binding.celsiusTx.setTextColor(resources.getColor(R.color.appMainColor))
                binding.fahrenheitTx.typeface = Typeface.DEFAULT
                binding.fahrenheitTx.setTextColor(resources.getColor(R.color.black))
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)
            }
        }
    }

}