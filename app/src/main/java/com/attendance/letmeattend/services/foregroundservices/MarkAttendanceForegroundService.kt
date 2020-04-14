package com.attendance.letmeattend.services.foregroundservices

import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast
import com.google.firebase.auth.FirebaseAuth



class MarkAttendanceForegroundService()  :  Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val myIntent = intent!!.getParcelableExtra<Intent>("intent")
        MyNotificationChannel.createAllNotificationChannels()
        val lectureBundle = myIntent!!.getBundleExtra("lecture")
        lectureBundle.setClassLoader(Lecture::class.java.getClassLoader())
        val lecture = lectureBundle.getParcelable<Lecture>("lecture")
        val lect_id = lecture.id
        val location = myIntent!!.getParcelableExtra<Location>("location")
        val sub_id = lecture.sub_id
        val inte = myIntent!!.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
        val builder : NotificationBuilder = NotificationBuilder()
        val database = FirebaseSetData(FirebaseAuth.getInstance().currentUser?.uid!!)
        Log.i("DatabaseStatus","Handling forground database work")
        val notif = builder.markAttendanceNotification(lecture)
        startForeground(lecture.id.hashCode(), notif)


        if (myIntent.action == builder.ACTION_YES)
        {
            Log.i("DatabaseStatus","Yes Clicked from intent service")
            val lat = myIntent.getDoubleExtra("lat",0.0)
            val lng = myIntent.getDoubleExtra("lng",0.0)
            database.addAttendance(lecture,1, intent)
            database.setLocation(lect_id,location)

        }
        else if (myIntent.action == builder.ACTION_NO)
        {
            Log.i("DatabaseStatus","No Clicked from intent service")
            database.addAttendance(lecture,0, intent)
        }
        else if (myIntent.action == builder.ACTION_NO_CLASS)
        {
            database.addAttendance(lecture, -1, intent)
            Log.i("DatabaseStatus","No Class Clicked from intent service")
        }


        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "FirebaseForegroundDBService created")
    }




    override fun onDestroy() {
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
        AppApplication?.context?.toast("Previous notification destroyed")
        Log.d(TAG, "FirebaseForegroundDBService ended")
        super.onDestroy()
    }
}