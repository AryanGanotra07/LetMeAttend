package com.attendance.letmeattend.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.attendance.letmeattend.R
import com.attendance.letmeattend.application.AppApplication

object MyNotificationChannel {

    val CHANNEL_ID = "AR123"

    fun createNotifChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppApplication.context?.getString(R.string.channel_name)
            val descriptionText = AppApplication?.context?.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
               AppApplication?.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}