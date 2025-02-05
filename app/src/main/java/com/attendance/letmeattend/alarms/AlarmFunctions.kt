package com.attendance.letmeattend.alarms

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.services.foregroundservices.MyForegroundService
import com.attendance.letmeattend.sharedpreferences.LocalRepository
import java.util.*


object AlarmFunctions {

    private val TAG = "AlarmFunction"

    fun execute(intent: Intent, shouldRun : Boolean) {

        val context = AppApplication?.context
        val alarmMgr =
            MyAlarmManager
        val builder = NotificationBuilder()

        val state= LocalRepository.getGeofenceState()
        val day = intent?.getIntExtra("day",1)
        val lectureBundle = intent?.getBundleExtra("lecture")
        lectureBundle.setClassLoader(Lecture::class.java.getClassLoader())
        val lect = lectureBundle?.getParcelable<Lecture>("lecture")
        val hour = intent?.getIntExtra("hour",0)
        val min = intent?.getIntExtra("min",0)
        Log.i("DETAILS-DAY",day.toString()+lect?.name)
        Log.i("DETAILS-HOUR",hour.toString())
        val calendar = Calendar.getInstance()
        Log.i("DETAILS-DAY-CURRENT",calendar.get(Calendar.DAY_OF_WEEK).toString())
        Log.i("DETAILS-HOUR-CURRENT",calendar.get(Calendar.HOUR_OF_DAY).toString())
        Log.d(TAG, shouldRun.toString())

            if (shouldRun)
             {
                Log.i("Called", "Yes")
               // context?.toast("Hello Recieved Message for " + day + ":" + hour)
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



                 //builder.removeNotification(lect!!.id.hashCode())
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     context?.startForegroundService(inte)
                 }
                 else {
                     context?.startService(inte)
                 }


                 val broadcastIntent = Intent("NEW_NOTIFICATION")
                 broadcastIntent.putExtra("intent",inte)
                 broadcastIntent.putExtra("lecture", bundle)
                 broadcastIntent.setPackage("com.attendance.letmeattend")
                 context?.sendBroadcast(broadcastIntent);


            } else {
                //context?.toast("Hello Recieved Message for nothing")
            }
//        else
//        {
//            builder.buildErrorNotif("Lecture time. " +
//                    "But as you are not near college. " +
//                    "Marked you absent. " +
//                    "To change it, click me.",-2)
//        }

        alarmMgr.setAlarm(lect!!,false)

    }

}