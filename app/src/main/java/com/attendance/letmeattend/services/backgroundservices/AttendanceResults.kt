package com.attendance.letmeattend.services.backgroundservices

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AttendanceResults() : Service() {
    override fun onBind(intent: Intent?): IBinder? {
       return null;
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

}