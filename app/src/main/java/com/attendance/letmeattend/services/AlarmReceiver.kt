package com.attendance.letmeattend.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*

class AlarmReceiver() : BroadcastReceiver() {
    private val builder = NotificationBuilder()
   private val context = AppApplication?.context
    private val alarmMgr = MyAlarmManager()

    override fun onReceive(context: Context?, intent: Intent?) {
      //context?.toast("Hello Recieved Message")
//        builder.buildNotification(1)
//        val intent = Intent(context,GoogleService::class.java)
//       context?.startService(intent)
        val day = intent?.getIntExtra("day",1)
        val lectureBundle = intent?.getBundleExtra("lecture")
        val lect = lectureBundle?.getParcelable<Lecture>("lecture")
        val hour = intent?.getIntExtra("hour",0)
        Log.i("DETAILS-DAY",day.toString()+lect?.name)
        Log.i("DETAILS-HOUR",hour.toString())
        val calendar = Calendar.getInstance()
        Log.i("DETAILS-DAY-CURRENT",calendar.get(Calendar.DAY_OF_WEEK).toString())
        Log.i("DETAILS-HOUR-CURRENT",calendar.get(Calendar.HOUR_OF_DAY).toString())
        if (hour == calendar.get(Calendar.HOUR_OF_DAY) && day == calendar.get(Calendar.DAY_OF_WEEK))
        {
            Log.i("Called","Yes")
            context?.toast("Hello Recieved Message for "+day+ ":"+hour)
            LocationService().enqueuework(context!!,intent!!)
        }
        else
        {
            context?.toast("Hello Recieved Message for nothing")
        }




    }






}
