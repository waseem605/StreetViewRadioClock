package com.liveearth.streetview.navigation.map.worldradio.locationTracking

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.LocationRepositoryStreetView
import com.liveearth.streetview.navigation.map.worldradio.R
import com.liveearth.streetview.navigation.map.worldradio.StreetViewCallBack.MyLocationListener
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView.ACTION_PLAY
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView.ACTION_STOP
import com.liveearth.streetview.navigation.map.worldradio.streetViewUtils.ConstantsStreetView.MY_TIMER_BROADCAST

@SuppressLint("LogNotTimber")
class LocationTrackingService : Service() {

    var walkSpeed:Float ?=null

    private var locationRepositoryStreetView: LocationRepositoryStreetView? = null
    val CHANNEL_ID = "NotificationService"
    val NOTIFICATION_ID = 4321
    var manager: NotificationManager? = null
    var custom_layout: RemoteViews? = null
    var notification: Notification? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // return super.onStartCommand(intent, flags, startId)

        intent?.let {
            when (it.action!!){
                ACTION_PLAY->{
                    Log.d("LocationTracking","=======start======")

                    startLocation()
                    locationNotificationPanel()
                }
                ACTION_STOP->{
                    Log.d("LocationTracking","========stop=====")

                    stopGettingLocation()
                    stopSelf()
                }
            }

        }
        return START_STICKY
    }

    private fun stopGettingLocation() {
        stopForeground(true)
        manager!!.cancel(NOTIFICATION_ID)
    }

    private fun startLocation() {
        locationRepositoryStreetView = LocationRepositoryStreetView(this,object :MyLocationListener{

            override fun onLocationChanged(location: Location) {
                location.let {
                Log.d("LocationTracking","=============")
                    walkSpeed = it.speed
                    Log.d("LocationTracking","============="+it.speed)

                }
            }

        })
        val intent = Intent(MY_TIMER_BROADCAST)
        intent.putExtra("speed", walkSpeed)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)


    }


    override fun onBind(intent: Intent?): IBinder? {


        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        super.onCreate()
        manager = getSystemService(NotificationManager::class.java)

    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            locationRepositoryStreetView!!.stopLocation()
        } catch (e: Exception) {
        }

    }

    private fun locationNotificationPanel() {
        createNotificationChannel()
        custom_layout = RemoteViews(packageName, R.layout.reminder_self_notification_small)
        custom_layout!!.setTextViewText(R.id.textNotification,walkSpeed.toString())
        val serviceIntent = Intent(this, LocationTrackingMainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notification = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(custom_layout)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(
            NOTIFICATION_ID,
            notification
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager!!.createNotificationChannel(serviceChannel)
        }
    }


}