package com.liveearth.streetview.navigation.map.worldradio.streetViewFragments

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.all.documents.files.reader.documentfiles.viewer.ads.PurchaseHelperStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.AdsStreetViewAds.AppPurchaseHelperStreetViewClock
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.activities.StreetViewPrivacyPolicyActivity
import com.liveearth.streetview.navigation.map.worldradio.databinding.FragmentSettingBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.StreetViewHelperAssistant

class SettingFragment : Fragment() {
    private lateinit var binding:FragmentSettingBinding
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater,container,false)
        mPreferenceManagerClass = PreferenceManagerClass(requireContext())
        binding.toolbarLt.backLink.visibility = View.INVISIBLE
        setThemeColor()
        clickListenerSetting()
        centerClickListener()

        return binding.root
    }

    private fun centerClickListener() {

        val billingHelper = AppPurchaseHelperStreetViewClock(requireContext())
        if (!billingHelper.shouldShowAds()){
            binding.buyPremiumCard.visibility = View.INVISIBLE
        }

        binding.privacyLT.setOnClickListener {
            val intent = Intent(requireContext(),StreetViewPrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        binding.feedBackLt.setOnClickListener {
            StreetViewHelperAssistant.appFeedbackFun(requireContext())
        }
        binding.moreAppsLt.setOnClickListener {
            StreetViewHelperAssistant.moreOurApp(requireContext())

        }
        binding.rateUsLt.setOnClickListener {
            StreetViewHelperAssistant.rateUsApp(requireContext())

        }
        binding.shareAppLt.setOnClickListener {
            StreetViewHelperAssistant.shareUsApp(requireContext())
        }
        binding.buyPremiumCard.setOnClickListener {
            PurchaseHelperStreetViewClock(requireContext()).purchaseStreetViewClockAdsPackage()
        }

    }

    private fun clickListenerSetting() {
        binding.toolbarLt.titleTx.text =getString(R.string.setting)

        //Distance Unit
        if (mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Miles,false)){
            binding.milesTx.typeface = Typeface.DEFAULT_BOLD
            binding.milesTx.setTextColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
            binding.switchUnit.isChecked = true
        }else{
            binding.meterTx.typeface = Typeface.DEFAULT_BOLD
            binding.meterTx.setTextColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
            binding.switchUnit.isChecked = false
        }

        //Distance Unit switch
        binding.switchUnit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.milesTx.typeface = Typeface.DEFAULT_BOLD
                binding.milesTx.setTextColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
                binding.meterTx.typeface = Typeface.DEFAULT
                binding.meterTx.setTextColor(resources.getColor(R.color.black))
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Miles,true)
            } else {
                binding.meterTx.typeface = Typeface.DEFAULT_BOLD
                binding.meterTx.setTextColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
                binding.milesTx.typeface = Typeface.DEFAULT
                binding.milesTx.setTextColor(resources.getColor(R.color.black))
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Miles,false)
            }
        }

        //Temperature Unit
        if (mPreferenceManagerClass.getBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)){
            binding.fahrenheitTx.typeface = Typeface.DEFAULT_BOLD
            binding.fahrenheitTx.setTextColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
            binding.switchUnitTemp.isChecked = true
        }else{
            binding.celsiusTx.typeface = Typeface.DEFAULT_BOLD
            binding.celsiusTx.setTextColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
            binding.switchUnitTemp.isChecked = false
        }

        //Temperature Unit switch
        binding.switchUnitTemp.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.fahrenheitTx.typeface = Typeface.DEFAULT_BOLD
                binding.fahrenheitTx.setTextColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
                binding.celsiusTx.typeface = Typeface.DEFAULT
                binding.celsiusTx.setTextColor(resources.getColor(R.color.black))
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,true)
            } else {
                binding.celsiusTx.typeface = Typeface.DEFAULT_BOLD
                binding.celsiusTx.setTextColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
                binding.fahrenheitTx.typeface = Typeface.DEFAULT
                binding.fahrenheitTx.setTextColor(resources.getColor(R.color.black))
                mPreferenceManagerClass.putBoolean(ConstantsStreetView.Unit_Is_Fahrenheit,false)
            }
        }
    }


    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")

        binding.settingFragmentBack.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.buyPremiumCard.setCardBackgroundColor(Color.parseColor(backgroundColor))
        binding.temperatureCard.setCardBackgroundColor(Color.parseColor(backgroundSecondColor))
        binding.distanceCard.setCardBackgroundColor(Color.parseColor(backgroundSecondColor))
        binding.centerCard.setCardBackgroundColor(Color.parseColor(backgroundSecondColor))
        binding.imageSwitchBack.setColorFilter(Color.parseColor(backgroundColor))
        binding.imageSwitchBackUnit.setColorFilter(Color.parseColor(backgroundColor))
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(backgroundColor))
        binding.toolbarLt.titleTx.setTextColor(Color.WHITE)
       //binding.switchUnitTemp.setBackgroundColor(Color.parseColor(backgroundColor))
    }


}