package com.attendance.letmeattend.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AlarmReceiver() : BroadcastReceiver() {
    private val builder = NotificationBuilder()
   private val context = AppApplication?.context
    private val alarmMgr = MyAlarmManager()

    override fun onReceive(context: Context?, intent: Intent?) {
      context?.toast("Hello Recieved Message")
//        builder.buildNotification(1)
//        val intent = Intent(context,GoogleService::class.java)
//       context?.startService(intent)

        LocationService().enqueuework(context!!,intent!!)




    }






}
