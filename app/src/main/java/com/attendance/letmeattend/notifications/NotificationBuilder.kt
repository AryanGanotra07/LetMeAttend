package com.attendance.letmeattend.notifications

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.attendance.letmeattend.R
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.notifications.MyNotificationChannel.CHANNEL_ID

class NotificationBuilder() {
    val context = AppApplication.context
    val ACTION_YES = "action_yes"
    val ACTION_NO = "action_no"

    fun buildNotification(intent : Intent, location : Location)

    {
        val id = intent.getIntExtra("intid",0)
        val yesIntent = Intent(context,NotificationReciever::class.java).apply {
            action = ACTION_YES
            putExtra(EXTRA_NOTIFICATION_ID, id)
        }
        val noIntent = Intent(context,NotificationReciever::class.java).apply {
            action = ACTION_NO
            putExtra(EXTRA_NOTIFICATION_ID,id)
        }
        val yesPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, yesIntent, 0)
        val noPendingIntent : PendingIntent =
            PendingIntent.getBroadcast(context,1,noIntent,0)
        var builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_create_black_24dp)
            .setContentTitle("Location")
            .setContentText(location.latitude.toString()+" "+location.longitude.toString())
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(location.latitude.toString()+" "+location.longitude.toString()))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_add_black_24dp,context.getString(R.string.yes),yesPendingIntent)
            .addAction(R.drawable.ic_add_black_24dp,context.getString(R.string.no),noPendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }
    }
    fun removeNotification(id: Int)
    {
        with(context?.let { NotificationManagerCompat.from(it) }) {
            // notificationId is a unique int for each notification that you must define
            this!!.cancel(id)
        }
    }
}