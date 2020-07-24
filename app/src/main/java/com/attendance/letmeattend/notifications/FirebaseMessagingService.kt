package com.attendance.letmeattend.notifications

import android.util.Log
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.LectureModel
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class FirebaseMessagingService() : FirebaseMessagingService() {
    private val TAG = "FirebaseMessaging"
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d(TAG, "Received" + p0.data.get("name"))
        val name = p0.data.get("name")
        val color = p0.data.get("color")
        val start_time = p0.data.get("start_time")
        val end_time = p0.data.get("end_time")
        val id = p0.data.get("id")
        NotificationBuilder().buildNewNotif(LectureModel(id!!.toInt(),0,start_time!!,end_time!!,name!!,color!!.toInt()))

    }
}