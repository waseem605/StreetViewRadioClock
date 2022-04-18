package com.liveearth.streetview.navigation.map.worldradio.streetViewFragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
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
        binding.toolbarLt.backBtnToolbar.setBackgroundColor(Color.parseColor(ConstantsStreetView.APP_SELECTED_COLOR))
        binding.toolbarLt.titleTx.text = "Premium"
        binding.toolbarLt.backLink.visibility = View.INVISIBLE
        setThemeColor()

        return binding.root
    }

    private fun setThemeColor() {
        val backgroundColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR, "#237157")
        val backgroundSecondColor = mPreferenceManagerClass.getString(ConstantsStreetView.APP_COLOR_Second, " #CDE6DD")
        Log.d("setThemeColor", "setThemeColor: $backgroundColor")
        binding.removeAdsBtn.setBackgroundColor(Color.parseColor(backgroundColor))
        //binding.contBack.setColorFilter(Color.parseColor(backgroundColor))
        binding.contBack.setColorFilter( Color.parseColor(backgroundColor) )
        binding.toolbarLt.backLink.setColorFilter(Color.parseColor(backgroundColor))

    }
}