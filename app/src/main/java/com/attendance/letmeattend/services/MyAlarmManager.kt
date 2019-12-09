package com.attendance.letmeattend.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.application.AppApplication.Companion.context
import java.util.*

class MyAlarmManager()  {

    private var alarmMgr : AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    init {
        alarmMgr= AppApplication?.context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(AppApplication?.context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(AppApplication?.context, 0, intent, 0)
        }
    }

    fun setAlarm()
    {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 14)
            set(Calendar.MINUTE,30)
        }

        alarmMgr?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            7*AlarmManager.INTERVAL_DAY,
            alarmIntent
        )

    }
}