package com.liveearth.streetview.navigation.map.worldradio.streetViewFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.liveearth.streetview.navigation.map.worldradio.databinding.FragmentThemeBinding

class ThemeFragment : Fragment() {
    private lateinit var binding: FragmentThemeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThemeBinding.inflate(layoutInflater, container, false)



        return binding.root
    }

}