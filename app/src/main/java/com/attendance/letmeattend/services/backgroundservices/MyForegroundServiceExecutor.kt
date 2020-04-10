package com.attendance.letmeattend.services.backgroundservices

import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.core.app.JobIntentService
import com.attendance.letmeattend.alarms.AlarmFunctions
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MyForegroundServiceExecutor() : JobIntentService() {


    private val TAG = MyForegroundServiceExecutor::class.java.simpleName



    fun enqueuework(context: Context, work: Intent) {
        FirebaseApp.initializeApp(context)
        val JOB_ID = work.getIntExtra("id",0)
        enqueueWork(context,
            MyForegroundServiceExecutor::class.java,JOB_ID,work)
        Log.d(TAG, "Executing helper")
    }


    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "Executing helper handle work")
        val myIntent = intent.getParcelableExtra<Intent>("intent")
            AlarmFunctions.execute(myIntent)

    }
}