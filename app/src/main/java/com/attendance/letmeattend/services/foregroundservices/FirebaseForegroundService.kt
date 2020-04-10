package com.attendance.letmeattend.services.foregroundservices

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.helpers.ForegroundServiceStatus
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth

class FirebaseForegroundService()  :  Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        MyNotificationChannel.createAllNotificationChannels()
        val notifBuilder: NotificationBuilder = NotificationBuilder()
        val lectureBundle = intent!!.getBundleExtra("lecture")
        val lecture = lectureBundle.getParcelable<Lecture>("lecture")
        val day = intent?.getIntExtra("day", 1)
        val hour = intent?.getIntExtra("hour", 0)
        val min = intent?.getIntExtra("min", 0)
        val id = intent.getIntExtra("id",0)
        var notif = notifBuilder.buildFirebaseNotification(lecture)
        startForeground(lecture.id.hashCode(),notif)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val db = FirebaseSetData(user.uid)
            db.getAttendanceStatus(lecture, intent)
        }
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "FirebaseForegroundService created")
    }




    override fun onDestroy() {
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
        AppApplication?.context?.toast("Previous notification destroyed")
        Log.d(TAG, "FirebaseForegroundService ended")
        super.onDestroy()
    }
}