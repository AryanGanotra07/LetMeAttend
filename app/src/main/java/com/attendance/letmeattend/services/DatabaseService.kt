package com.attendance.letmeattend.services

import android.content.Context
import android.content.Intent
import android.location.Location
import androidx.core.app.JobIntentService
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DatabaseService() : JobIntentService() {



    private val JOB_ID = 10
    fun enqueuework(context: Context, work: Intent) {
        FirebaseApp.initializeApp(context)
        enqueueWork(context,DatabaseService::class.java,JOB_ID,work)
    }

    override fun onHandleWork(intent: Intent) {
        val lectureBundle = intent.getBundleExtra("lecture")
        val lecture = lectureBundle.getParcelable<Lecture>("lecture")
        val lect_id = lecture.id
        val location = intent.getParcelableExtra<Location>("location")
        val sub_id = lecture.sub_id
        val builder : NotificationBuilder = NotificationBuilder()
        val database = FirebaseSetData(FirebaseAuth.getInstance().currentUser?.uid!!)

        if (intent.action == builder.ACTION_YES)
        {
            val lat = intent.getDoubleExtra("lat",0.0)
            val lng = intent.getDoubleExtra("lng",0.0)
            database.addAttendance(lect_id,sub_id,true)
            database.setLocation(lect_id,location)
        }
        else if (intent.action == builder.ACTION_NO)
        {
            database.addAttendance(lect_id,sub_id,false)
        }
        else if (intent.action == builder.ACTION_NO_CLASS)
        {

        }

    }
}