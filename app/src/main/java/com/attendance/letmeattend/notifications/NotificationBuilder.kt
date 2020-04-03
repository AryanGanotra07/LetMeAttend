package com.attendance.letmeattend.notifications

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
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
    final val ENTRY_EXIT_NOTIF_ID = 1123
    private lateinit var inte : Intent

    fun buildNotification(intent : Intent, location : Location) : Notification

    {
        inte = intent
        val id = intent.getIntExtra("intid",0)
        val lectureBundle = intent.getBundleExtra("lecture")
        val lecture = lectureBundle.getParcelable<Lecture>("lecture")
        val lect_id = lecture.id
        val sub_id = lecture.sub_id
        val name = lecture.name
        Log.i("LECTURE_ID_BUILDER",lect_id)

        val bundle = Bundle()
        bundle.putParcelable("lecture",lecture)

        val yesIntent = Intent(context,NotificationReciever::class.java)
        yesIntent.action = ACTION_YES
        yesIntent.putExtra("EXTRA_NOTIFICATION_ID", id)
        yesIntent.putExtra("lecture",bundle)
       // yesIntent.putExtra("lat",location.latitude)
        yesIntent.putExtra("location",location)
        yesIntent.putExtra(Intent.EXTRA_INTENT,intent)

        val noIntent = Intent(context,NotificationReciever::class.java)
        noIntent.action = ACTION_NO
        noIntent.putExtra("EXTRA_NOTIFICATION_ID",id)
        noIntent.putExtra("lecture",bundle)
        noIntent.putExtra(Intent.EXTRA_INTENT,intent)
        //noIntent.putExtra("lat",location.latitude)
        //noIntent.putExtra("lng",location.longitude)

        val noClassIntent = Intent(context,NotificationReciever::class.java)
        noClassIntent.action = ACTION_NO_CLASS
        noClassIntent.putExtra("EXTRA_NOTIFICATION_ID",id)
        noClassIntent.putExtra("lecture", bundle)
        noClassIntent.putExtra(Intent.EXTRA_INTENT,intent)


        val yesPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 0, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val noPendingIntent : PendingIntent =
            PendingIntent.getBroadcast(context,1,noIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val noClassPendingIntent : PendingIntent =
            PendingIntent.getBroadcast(context,2,noClassIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_create_black_24dp)
            .setContentTitle(name)
            .setContentText("Have you attended "+lecture.name+" today from "+lecture.s_time+ " to "+lecture.e_time+"?")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Have you attended "+lecture.name+" today from "+lecture.s_time+ " to "+lecture.e_time+"?"))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(R.drawable.ic_add_black_24dp,context.getString(R.string.yes),yesPendingIntent)
            .addAction(R.drawable.ic_add_black_24dp,context.getString(R.string.no),noPendingIntent)
            .addAction(R.drawable.ic_add_black_24dp,context.getString(R.string.no_class),noClassPendingIntent)
            .setColor(lecture.color)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }

        return builder.build()
    }
    fun removeNotification(id: Int)
    {
        with(context?.let { NotificationManagerCompat.from(it) }) {
            // notificationId is a unique int for each notification that you must define
            this!!.cancel(id)
        }

      //  context?.stopService(inte)
    }

    fun buildErrorNotif(err : String,id : Int) : Notification
    {
        var builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_create_black_24dp)
            .setContentTitle("Error")
            .setContentText(err)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(err))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
        val notif = builder.build()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, notif)
        }
        return notif
    }

    fun buildEnterLocationNotification (message : String, state : String, id : Int) : Notification {
        var builder = NotificationCompat.Builder(context!!,MyNotificationChannel.ENTRANCE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_create_black_24dp)
            .setContentTitle("Let Me Attend")
            .setContentText("You have "+state+" college")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(message))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
        val notif = builder.build()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, notif)
        }
        return notif
    }
}