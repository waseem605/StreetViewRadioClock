package com.liveearth.streetview.navigation.map.worldradio.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityStreeViewTravelExpenseBinding

class StreetViewTravelExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStreeViewTravelExpenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStreeViewTravelExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


}