package com.liveearth.streetview.navigation.map.worldradio.streetViewFragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.FragmentPremiumBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.PreferenceManagerClass

class PremiumFragment : Fragment() {
    private lateinit var binding: FragmentPremiumBinding
    private lateinit var mPreferenceManagerClass: PreferenceManagerClass


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPremiumBinding.inflate(layoutInflater, container, false)
        mPreferenceManagerClass = PreferenceManagerClass(requireContext())
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(resources.getColor(R.color.white))
        setThemeColor()

        return binding.root
    }

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        /*   val window: Window = requireActivity().window
           window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
           window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
           window.statusBarColor = Color.parseColor(backgroundColor)*/
        binding.removeAdsBtn.setBackgroundColor(Color.parseColor(backgroundColor))
        //binding.contBack.setColorFilter(Color.parseColor(backgroundColor))
        binding.contBack.setColorFilter( Color.parseColor(backgroundColor) )
        binding.toolbarLt.backLink.setColorFilter(Color.parseColor(backgroundColor))
        binding.toolbarLt.titleTx.setTextColor(Color.parseColor(backgroundColor))
    }
}