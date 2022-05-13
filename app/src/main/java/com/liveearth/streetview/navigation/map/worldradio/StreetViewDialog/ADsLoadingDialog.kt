package com.liveearth.streetview.navigation.map.worldradio.StreetViewDialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.liveearth.streetview.navigation.map.worldradio.databinding.DialogAdsLoadingBinding

class ADsLoadingDialog(val mContext: Context): Dialog(mContext) {
    lateinit var binding: DialogAdsLoadingBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        window !!.requestFeature(Window.FEATURE_NO_TITLE)
        window !!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DialogAdsLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
}