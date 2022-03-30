package com.example.centurionnavigation.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExistCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.DialogLocationRequestBinding

class LocationRequestDialogueBox(val mContext: Context, val listener: ExistCallBackListener):Dialog(mContext) {
    lateinit var binding: DialogLocationRequestBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding=DialogLocationRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.yesTx.setOnClickListener {
            dismiss()
            listener.onExistClick()
        }

    }

}