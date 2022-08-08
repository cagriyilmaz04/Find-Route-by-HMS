package com.example

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationServices

class Service:Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
    // Define a fusedLocationProviderClientEdit object.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
// Instantiate the fusedLocationProviderClient object.

    private fun enableBackgroundLocation(){
        val builder: Notification.Builder
        val mNotification: Notification
        var notificationId = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = packageName
            val notificationChannel =
                NotificationChannel(channelId, "LOCATION", NotificationManager.IMPORTANCE_LOW)
            notificationManager.createNotificationChannel(notificationChannel)
            Notification.Builder(this, channelId)
        } else {
            Notification.Builder(this)
        }
        mNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.build()
        } else {
            builder.notification
        }
        //fusedLocationProviderClient.enableBackgroundLocation(notificationId,mNotification)



    }
}