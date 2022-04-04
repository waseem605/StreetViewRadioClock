package com.liveearth.streetview.navigation.map.worldradio.activities

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.centurionnavigation.dialogs.LocationEnableRequestDialogueBox
import com.example.centurionnavigation.dialogs.LocationRequestDialogueBox
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.ExistCallBackListener
import com.liveearth.streetview.navigation.map.worldradio.databinding.ActivityBaseStreetViewBinding
import java.lang.Exception

open class BaseStreetViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseStreetViewBinding
    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseStreetViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkStreetViewLocation(this)==false) {
            val dialogueBox = LocationEnableRequestDialogueBox(this)
            dialogueBox.show()
        }



        if (checkLocationPermission()){
            checkLocationPermission()
        }

    }

    fun setToast(context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    private fun checkStreetViewLocation(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


    override fun onResume() {
        super.onResume()
        if (checkStreetViewLocation(this)==false) {
            val dialogueBox = LocationEnableRequestDialogueBox(this)
            dialogueBox.show()
        }

        if (checkLocationPermission()){
            checkLocationPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //mainItemClickListener()
            }
        } else {
            Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
            if (! ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", this.packageName, null),
                    ),
                )
            }
        }
    }

    private fun checkLocationPermission():Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                val permissionDialog = LocationRequestDialogueBox(this,object :
                    ExistCallBackListener {
                    override fun onExistClick() {
                        requestLocationPermission()
                    }
                })
                permissionDialog.show()

            } else {
                requestLocationPermission()
            }
            return false
        }else{
            return true
            //mainItemClickListener()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }




    fun showProgressDialog(mContext: Context){
        try {
            mProgressDialog = Dialog(mContext)
            mProgressDialog.setContentView(R.layout.dialog_progress)
            mProgressDialog.setCancelable(true)
            mProgressDialog.setCanceledOnTouchOutside(false)
            mProgressDialog.show()
        } catch (e: Exception) {
        }
    }

    fun hideProgressDialog(mContext: Context){
        try {
            //mProgressDialog = Dialog(mContext)
            Log.d("mProgressDialog", "hideProgressDialog: =========================")
            mProgressDialog.dismiss()
        } catch (e: Exception) {
        }
    }

}