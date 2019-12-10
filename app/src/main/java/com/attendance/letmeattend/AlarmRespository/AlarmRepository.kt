package com.attendance.letmeattend.AlarmRespository

import com.attendance.letmeattend.models.Alarm
import com.attendance.letmeattend.models.Lecture

object AlarmRepository {

    private val alarms : ArrayList<Alarm> = ArrayList()

    fun addAlarm(alarm : Alarm)
    {
        alarms.add(alarm)
    }
    fun deleteAlarm(id : String): Alarm
    {
        val alarm = getAlarm(id)
       if (alarm!=null)
       {
           alarms.remove(alarm)
       }
        return alarm!!
    }
    fun getAlarm(id : String) : Alarm
    {
        val alarm  = alarms.find { alarm -> alarm.lecture.id == id }
        return alarm!!
    }
    fun getAlarms() : ArrayList<Alarm>
    {
        return alarms
    }
}
