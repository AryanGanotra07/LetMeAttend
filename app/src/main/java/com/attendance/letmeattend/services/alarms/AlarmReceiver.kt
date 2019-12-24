package com.attendance.letmeattend.services.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.services.MyForegroundService
import com.attendance.letmeattend.utils.toast
import java.util.*

class AlarmReceiver() : BroadcastReceiver() {



    override fun onReceive(context: Context?, intent: Intent?) {
      //context?.toast("Hello Recieved Message")
//        builder.buildNotification(1)
//        val intent = Intent(context,GoogleService::class.java)
//       context?.startService(intent)

        val context = AppApplication?.context
        val alarmMgr =
            MyAlarmManager
        val builder = NotificationBuilder()
        val preference=context?.getSharedPreferences("GEOFENCE", Context.MODE_PRIVATE)
        val state= preference?.getBoolean("name",true)
        val day = intent?.getIntExtra("day",1)
        val lectureBundle = intent?.getBundleExtra("lecture")
        val lect = lectureBundle?.getParcelable<Lecture>("lecture")
        val hour = intent?.getIntExtra("hour",0)
        val min = intent?.getIntExtra("min",0)
        Log.i("DETAILS-DAY",day.toString()+lect?.name)
        Log.i("DETAILS-HOUR",hour.toString())
        val calendar = Calendar.getInstance()
        Log.i("DETAILS-DAY-CURRENT",calendar.get(Calendar.DAY_OF_WEEK).toString())
        Log.i("DETAILS-HOUR-CURRENT",calendar.get(Calendar.HOUR_OF_DAY).toString())
        if (state!!) {
            if (hour == calendar.get(Calendar.HOUR_OF_DAY) && day == calendar.get(Calendar.DAY_OF_WEEK)) {
                Log.i("Called", "Yes")
                context?.toast("Hello Recieved Message for " + day + ":" + hour)
                //LocationService().enqueuework(context!!,intent!!)
                val inte = Intent(context, MyForegroundService::class.java)
                inte.putExtra("day", day)
                inte.putExtra("hour", hour)
                inte.putExtra("min", min)
//        intent.putExtra("id",lecture.id)
//        intent.putExtra("sub_id",lecture.sub_id)
                val bundle = Bundle()
                bundle.putParcelable("lecture", lect)
                inte.putExtra("lecture", bundle)
                inte.putExtra("intid", lect!!.id.hashCode())
                context?.startService(inte)


            } else {
                context?.toast("Hello Recieved Message for nothing")
            }
        }
        else
        {
            builder.buildErrorNotif("Lecture time. " +
                    "But as you are not near college. " +
                    "Marked you absent. " +
                    "To change it, click me.",-2)
        }

        alarmMgr.setAlarm(lect!!,false)





    }








}
