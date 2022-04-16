package com.example.centurionnavigation.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.RatingBar
import android.widget.Toast
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExistCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.DialogRateAppBinding
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.SettingHelperStreetView

class ExitDialogueBoxStreetView(val mContext: Context, val listener: ExistCallBackListener):Dialog(mContext) {
    lateinit var binding: DialogRateAppBinding

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding=DialogRateAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //nativeAd()

        val sharedPreferences: SharedPreferences = mContext.getSharedPreferences(ConstantsStreetView.sharedPrefName, Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()!!
        val spanCount = sharedPreferences.getInt(ConstantsStreetView.RATE_APP,0)

        if (spanCount == 1){
            binding.simpleRatingBar.visibility = View.GONE
            binding.dDescriptionTv.visibility = View.GONE
            binding.dTitleTv.text = context.getString(R.string.exist_app_dialog)
        }else {

            binding.simpleRatingBar.setOnRatingBarChangeListener(object :
                RatingBar.OnRatingBarChangeListener {
                override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                    if (p1 >= 4) {
                        editor.putInt(ConstantsStreetView.RATE_APP,1)
                        editor.apply()
                        editor.commit()
                        SettingHelperStreetView.rateUsApp(mContext)
                        dismiss()

                    } else {
                        Toast.makeText(mContext, "Thank you", Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                }
            })
        }

        binding.dismissTx.setOnClickListener {
            dismiss()
        }

    /*    binding.yesTx.setOnClickListener {
            dismiss()
            listener.onExistClick()
        }*/

    }

  /*  private fun nativeAd() {
        EarthLiveMapMyAppNativeAds.loadWeatherAdmobNative(mContext, binding.natvLay.flAdplaceholder)
    }*/

}