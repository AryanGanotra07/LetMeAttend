package com.attendance.letmeattend.notifications

import android.util.Log
import com.attendance.letmeattend.models.Lecture
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class FirebaseMessagingService() : FirebaseMessagingService() {
    private val TAG = "FirebaseMessaging"
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d(TAG, "Received")

    }
}