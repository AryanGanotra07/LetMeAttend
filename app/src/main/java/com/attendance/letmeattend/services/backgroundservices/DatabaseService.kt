package com.attendance.letmeattend.services.backgroundservices

import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.core.app.JobIntentService
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class DatabaseService() : JobIntentService() {




    fun enqueuework(context: Context, work: Intent) {
        FirebaseApp.initializeApp(context)
        val JOB_ID = work.getIntExtra("id",0)
        enqueueWork(context,
            DatabaseService::class.java,JOB_ID,work)
    }

    override fun onHandleWork(intent: Intent) {
        MyNotificationChannel.createAllNotificationChannels()
        val lectureBundle = intent.getBundleExtra("lecture")
        val lecture = lectureBundle.getParcelable<Lecture>("lecture")
        val lect_id = lecture.id
        val location = intent.getParcelableExtra<Location>("location")
        val sub_id = lecture.sub_id
        val builder : NotificationBuilder = NotificationBuilder()
        val database = FirebaseSetData(FirebaseAuth.getInstance().currentUser?.uid!!)
        Log.i("DatabaseStatus","Handling database work")

        if (intent.action == builder.ACTION_YES)
        {
            Log.i("DatabaseStatus","Yes Clicked from intent service")
            val lat = intent.getDoubleExtra("lat",0.0)
            val lng = intent.getDoubleExtra("lng",0.0)
            database.addAttendance(lecture,true)
            database.setLocation(lect_id,location)

        }
        else if (intent.action == builder.ACTION_NO)
        {
            Log.i("DatabaseStatus","No Clicked from intent service")
            database.addAttendance(lecture,false)
        }
        else if (intent.action == builder.ACTION_NO_CLASS)
        {
            Log.i("DatabaseStatus","No Class Clicked from intent service")
        }

    }
}