package com.attendance.letmeattend.services.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast

class BootCompleteReciever() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        MyNotificationChannel.createNotifChannel()
        val notifBuilder = NotificationBuilder()
        notifBuilder.buildErrorNotif("Boot completed",-2)
        Log.i("Boot","COmpleted")
        context?.toast("Boot completed")
        val intent = Intent(context,BootCompleteReciever::class.java)
        intent.putExtra("e","22")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(intent)
        }
        else
        {
            context?.startService(intent)
        }
    }
}