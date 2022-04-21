package com.example.centurionnavigation.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import com.liveearth.streetview.navigation.map.worldradio.databinding.DialogLocationEnableRequestBinding

class EnableInternetConnectionRequestDialogueBox(val mContext: Context):Dialog(mContext) {
    lateinit var binding: DialogLocationEnableRequestBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding=DialogLocationEnableRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.locationYesTx.setOnClickListener {
            dismiss()
            try {
                val callGPSSettingIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                mContext.startActivity(callGPSSettingIntent)
            } catch (e: Exception) {
            }
        }

    }

}