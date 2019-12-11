package com.attendance.letmeattend.notifications

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.attendance.letmeattend.R
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.MyNotificationChannel.CHANNEL_ID

class NotificationBuilder() {
    val context = AppApplication.context
    val ACTION_YES = "action_yes"
    val ACTION_NO = "action_no"
    val ACTION_NO_CLASS = "action_no_class"

    fun buildNotification(intent : Intent, location : Location)

    {
        val id = intent.getIntExtra("intid",0)
        val lectureBundle = intent.getBundleExtra("lecture")
        val lecture = lectureBundle.getParcelable<Lecture>("lecture")
        val lect_id = lecture.id
        val sub_id = lecture.sub_id
        val name = lecture.name
        Log.i("LECTURE_ID_BUILDER",lect_id)

        val yesIntent = Intent(context,NotificationReciever::class.java)
        yesIntent.action = ACTION_YES
        yesIntent.putExtra("EXTRA_NOTIFICATION_ID", id)
        val yesBundle = Bundle()
        yesBundle.putParcelable("lecture",lecture)
        yesIntent.putExtra("lecture",yesBundle)

        val noIntent = Intent(context,NotificationReciever::class.java)
        noIntent.action = ACTION_NO
        noIntent.putExtra("EXTRA_NOTIFICATION_ID",id)
        val noBundle = Bundle()
        noBundle.putParcelable("lecture",lecture)
        noIntent.putExtra("lecture",noBundle)

        val noClassIntent = Intent(context,NotificationReciever::class.java)
        noClassIntent.action = ACTION_NO_CLASS

        val yesPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val noPendingIntent : PendingIntent =
            PendingIntent.getBroadcast(context,1,noIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_create_black_24dp)
            .setContentTitle(name)
            .setContentText(location.latitude.toString()+" "+location.longitude.toString())
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(location.latitude.toString()+" "+location.longitude.toString()))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addExtras(Bundle())
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