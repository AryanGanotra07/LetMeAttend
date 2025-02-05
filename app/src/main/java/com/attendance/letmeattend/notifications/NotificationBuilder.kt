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
import com.attendance.letmeattend.activities.ChangeAttendanceActivity
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.MyNotificationChannel.CHANNEL_ID
import java.util.*

class NotificationBuilder() {
    val context = AppApplication.context
    val ACTION_YES = "action_yes"
    val ACTION_NO = "action_no"
    val ACTION_NO_CLASS = "action_no_class"
    final val ENTRY_EXIT_NOTIF_ID = 1123
    val ATTENDANCE_STATUS_NOTIF_ID = 1124
    private lateinit var inte : Intent
    val EXTRA_NOTIFICATION_ID = "extraNotifId"
    private val TAG = "NotificationBuilder"


    fun buildNotification(intent : Intent, location : Location) : Notification

    {
        inte = intent
        val id = intent.getIntExtra("intid",0)
        val lectureBundle = intent.getBundleExtra("lecture")
        val lecture = lectureBundle.getParcelable<Lecture>("lecture")
        val lect_id = lecture.id
        val sub_id = lecture.sub_id
        val name = lecture.name


        val bundle = Bundle()
        bundle.putParcelable("lecture",lecture)

        val yesIntent = Intent(context,NotificationReciever::class.java)
        yesIntent.action = ACTION_YES
        yesIntent.putExtra(EXTRA_NOTIFICATION_ID, id)
        yesIntent.putExtra("lecture",bundle)
       // yesIntent.putExtra("lat",location.latitude)
        yesIntent.putExtra("location",location)
        yesIntent.putExtra(Intent.EXTRA_INTENT,intent)

        val noIntent = Intent(context,NotificationReciever::class.java)
        noIntent.action = ACTION_NO
        noIntent.putExtra(EXTRA_NOTIFICATION_ID,id)
        noIntent.putExtra("lecture",bundle)
        noIntent.putExtra(Intent.EXTRA_INTENT,intent)
        //noIntent.putExtra("lat",location.latitude)
        //noIntent.putExtra("lng",location.longitude)

        val noClassIntent = Intent(context,NotificationReciever::class.java)
        noClassIntent.action = ACTION_NO_CLASS
        noClassIntent.putExtra(EXTRA_NOTIFICATION_ID,id)
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
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setColor(lecture.color)
            .setVibrate(
                longArrayOf(
                    100,
                    200,
                    300,
                    400,
                    500,
                    400,
                    300,
                    200,
                    400
                )
            )
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
        }
        Log.i(TAG,"Building notification with id" + id)
        return builder.build()
    }
    fun removeNotification(id: Int)
    {
        Log.d(TAG, "Removing notification with id.. " + id)
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
            .setVibrate(
                longArrayOf(
                    100,
                    200,
                    300,
                    400,
                    500,
                    400,
                    300,
                    200,
                    400
                )
            )
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
            .setVibrate(
            longArrayOf(
                100,
                200,
                300,
                400,
                500,
                400,
                300,
                200,
                400
            )
        )
        val notif = builder.build()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, notif)
        }
        return notif
    }

    fun buildAttendanceStatusNotification(lecture : Lecture, attended : Int, id : Int) : Notification {

        var message = "Present! You have been marked present for lecture - " + lecture.name
        if (attended == 0) message = "Absent!! You should regularly attend classes dude - " + lecture.name
        else if(attended == -1) message = "No Class! Definitely some good times for you!-"  + lecture.name
        var builder = NotificationCompat.Builder(context!!,MyNotificationChannel.ATTENDANCE_STATUS_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_create_black_24dp)
            .setContentTitle("Let Me Attend")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(message))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_MAX)

            .setVibrate(
                longArrayOf(
                    100,
                    200,
                    300,
                    400,
                    500,
                    400,
                    300,
                    200,
                    400
                )
            )
        val notif = builder.build()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, notif)
        }
        return notif
    }

    fun buildNoResponseNotification(lecture: Lecture) : Notification {
        val bundle = Bundle()
        bundle.putParcelable("lecture",lecture)
        val notifyIntent = Intent(context, ChangeAttendanceActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            action = "MARK_ATTENDANCE_ACTIVITY-"+lecture.id
            putExtra("lecture", bundle)
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val id = Random().nextInt(1000)
        var builder = NotificationCompat.Builder(context!!,MyNotificationChannel.NO_RESPONSE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_create_black_24dp)
            .setContentTitle(lecture.name)
            .setContentText("Marked you absent for your lecture from "+lecture.s_time+" to "+lecture.e_time + "as we got no response from you.")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("Marked you absent for your lecture from "+lecture.s_time+" to "+lecture.e_time + "as we got no response from you."))
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(notifyPendingIntent)
            .setVibrate(
                longArrayOf(
                    100,
                    200,
                    300,
                    400,
                    500,
                    400,
                    300,
                    200,
                    400
                )
            )
        val notif = builder.build()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(lecture.id.hashCode()-1, notif)
        }
        return notif
    }

    fun buildFirebaseNotification(lecture : Lecture) : Notification {
        var message = "Checking attendance status for " + lecture.name + " from " + lecture.s_time + " to " + lecture.e_time
        var builder = NotificationCompat.Builder(context!!,MyNotificationChannel.FIREBASE_FOREGROUND_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_create_black_24dp)
            .setContentTitle("Let Me Attend")
            .setContentText(message)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_MIN)
        val notif = builder.build()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(lecture.id.hashCode(), notif)
        }
        return notif
    }

    fun markAttendanceNotification(lecture : Lecture) : Notification {
        var message = "Marking attendance for " + lecture.name + " from " + lecture.s_time + " to " + lecture.e_time
        var builder = NotificationCompat.Builder(context!!,MyNotificationChannel.MARK_ATTENDANCE_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_create_black_24dp)
            .setContentTitle("Let Me Attend")
            .setContentText(message)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setPriority(NotificationCompat.PRIORITY_MIN)
        val notif = builder.build()
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(lecture.id.hashCode()-2, notif)
        }
        return notif
    }
}