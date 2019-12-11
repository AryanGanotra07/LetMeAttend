package com.attendance.letmeattend.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.models.Lecture
import java.math.BigInteger
import java.util.*

class MyAlarmManager()  {

    private var alarmMgr : AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    private lateinit var intent : Intent

    init {
        alarmMgr= AppApplication?.context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager


    }

    fun setAlarm(lecture: Lecture)
    {
        val day  = lecture.day
        val temp = lecture.s_time.split(":")
        val hour = temp[0].toInt()
        val min = temp[1].toInt()
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_WEEK,day)
            set(Calendar.HOUR_OF_DAY, hour)
           set(Calendar.MINUTE,min)
        }

        Log.i("AlarmStatus","ADDED:"+lecture.id)

//        if(PendingIntent.getBroadcast(
//                AppApplication?.context, 0,
//                intent,
//                PendingIntent.FLAG_NO_CREATE) == null)
//        {
           intent =  Intent(AppApplication?.context, AlarmReceiver::class.java)
      //  intent.putExtra("name",lecture.name)
        intent.putExtra("day",day)
        intent.putExtra("hour",hour)
        intent.putExtra("min",min)
//        intent.putExtra("id",lecture.id)
//        intent.putExtra("sub_id",lecture.sub_id)
        val bundle = Bundle()
        bundle.putParcelable("lecture",lecture)
        intent.putExtra("lecture",bundle)
        intent.putExtra("intid", lecture.id.hashCode())
            alarmIntent =intent.let { intent ->

                val id = getInt(lecture.id)
                PendingIntent.getBroadcast(AppApplication?.context,id , intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            alarmMgr?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                7*AlarmManager.INTERVAL_DAY,
                alarmIntent
            )
        // }
    }

    fun removeAlarm(lecture: Lecture)
    {
        val day  = lecture.day + 1
        val temp = lecture.s_time.split(":")
        val hour = temp[0].toInt()
        val min = temp[1].toInt()
        intent =  Intent(AppApplication?.context, AlarmReceiver::class.java)
        //intent.putExtra("name",lecture.name)
        intent.putExtra("day",day)
        intent.putExtra("hour",hour)
        intent.putExtra("min",min)
//        intent.putExtra("id",lecture.id)
//        intent.putExtra("sub_id",lecture.sub_id)
        val bundle = Bundle()
        bundle.putParcelable("lecture",lecture)
        intent.putExtra("lecture",bundle)
        intent.putExtra("intid", lecture.id.hashCode())
        alarmIntent =intent.let { intent ->
            val id = getInt(lecture.id)
            PendingIntent.getBroadcast(AppApplication?.context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmMgr?.cancel(alarmIntent)


    }

    fun getInt(string : String) : Int
    {
        return string.hashCode()
    }

}