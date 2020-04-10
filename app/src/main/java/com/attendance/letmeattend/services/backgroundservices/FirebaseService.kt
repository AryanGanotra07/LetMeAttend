package com.attendance.letmeattend.services.backgroundservices

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.attendance.letmeattend.alarms.AlarmReceiver
import com.attendance.letmeattend.alarms.MyAlarmManager
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.models.Lecture
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import kotlinx.coroutines.awaitAll

class FirebaseService(val context  : Context,val workerParams : WorkerParameters) : Worker(context, workerParams)  {



    private val TAG = "FirebaseService"
    override fun doWork(): Result {
        val lectureString = inputData.getString("lecture")
        Log.d(TAG, lectureString)
        val gson = Gson()
        val lecture = gson.fromJson<Lecture>(lectureString, Lecture::class.java)
        val day = inputData.getInt("day", 0)
        val hour = inputData.getInt("hour", 0)
        val min = inputData.getInt("min", 0)
        val intid = inputData.getInt("intid", lecture!!.id.hashCode())


        val intent =  Intent(AppApplication?.context, AlarmReceiver::class.java)
        //  intent.putExtra("name",lecture.name)
       intent.putExtra("day",day)
        intent.putExtra("hour",hour)
        intent.putExtra("min",min)
        intent.putExtra("intid", intid)
        val bundle = Bundle()
        bundle.putParcelable("lecture",lecture!!)
        intent.putExtra("lecture", bundle)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val db = FirebaseSetData(user.uid)
            db.getAttendanceStatus(lecture, intent)
        }
       // Repository.getAttendanceStatus(lecture!!, intent)


        return Result.success()
    }




}