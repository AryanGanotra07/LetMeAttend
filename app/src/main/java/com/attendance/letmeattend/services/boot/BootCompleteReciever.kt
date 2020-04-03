package com.attendance.letmeattend.services.boot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast


class BootCompleteReciever() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        MyNotificationChannel.createAllNotificationChannels()
        val notifBuilder = NotificationBuilder()
        notifBuilder.buildErrorNotif("Boot completed",-2)
        Log.i("Boot","COmpleted")
        context?.toast("Boot completed")
        val myIntent = Intent(context, BootCompleteService::class.java)
        context!!.startService(myIntent)

//        val inte = Intent(context,BootCompleteReciever::class.java)
//        inte.putExtra("e","22")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context?.startForegroundService(inte)
//        }
//        else
//        {
//            context?.startService(inte)
//        }
    }
}