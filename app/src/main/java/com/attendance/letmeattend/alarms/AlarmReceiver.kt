package com.attendance.letmeattend.alarms

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.helpers.ForegroundServiceStatus
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.services.backgroundservices.FirebaseService
import com.attendance.letmeattend.services.foregroundservices.FirebaseForegroundService
import com.attendance.letmeattend.services.foregroundservices.MyForegroundService
import com.attendance.letmeattend.sharedpreferences.LocalRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import java.util.*


class AlarmReceiver() : BroadcastReceiver() {


    private val TAG = "AlarmReceiver"

    @SuppressLint("RestrictedApi")
    override fun onReceive(context: Context?, intent: Intent?) {

        val wakeLock: PowerManager.WakeLock =
            (context!!.getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                    acquire()
                }
            }
        val state = LocalRepository.getGeofenceState()
        val day = intent?.getIntExtra("day", 1)
        val lectureBundle = intent?.getBundleExtra("lecture")
        val lect = lectureBundle?.getParcelable<Lecture>("lecture")
        val hour = intent?.getIntExtra("hour", 0)
        val min = intent?.getIntExtra("min", 0)
        Log.i("DETAILS-DAY", day.toString() + lect?.name)
        Log.i("DETAILS-HOUR", hour.toString())
        Log.i("DETAILS-MIN", min.toString())
        val calendar = Calendar.getInstance()
        Log.i("DETAILS-DAY-CURRENT", calendar.get(Calendar.DAY_OF_WEEK).toString())
        Log.i("DETAILS-HOUR-CURRENT", calendar.get(Calendar.HOUR_OF_DAY).toString())
        Log.i("DETAILS-MIN-CURRENT", calendar.get(Calendar.MINUTE).toString())
        Log.d(TAG, state.toString())

        if (hour == calendar.get(Calendar.HOUR_OF_DAY)
            && day == calendar.get(Calendar.DAY_OF_WEEK)
            && min == calendar.get(Calendar.MINUTE)
        ) {

//                val gson = Gson()
//                val lectureString = gson.toJson(lect!!)
//                val builder = Data.Builder()
//                builder.putString("lecture", lectureString)
//                builder.putInt("day", day)
//                builder.putInt("hour",hour)
//                builder.put("min", min)
//                builder.putInt("intid", lect!!.id.hashCode())
//
//                val data = builder.build()
//
//                val uploadWorkRequest = OneTimeWorkRequestBuilder<FirebaseService>()
//                    .setInputData(data)
//                    .build()
//
//
//                WorkManager.getInstance(context).enqueue(uploadWorkRequest)


                //Repository.getAttendanceStatus(lect!!, intent)

            val inte = Intent(context, FirebaseForegroundService::class.java)
            inte.putExtra("day", day)
            inte.putExtra("hour", hour)
            inte.putExtra("min", min)
//        intent.putExtra("id",lecture.id)
//        intent.putExtra("sub_id",lecture.sub_id)
            val bundle = Bundle()
            bundle.putParcelable("lecture", lect)
            inte.putExtra("lecture", bundle)
            inte.putExtra("intid", lect!!.id.hashCode())


            if (!ForegroundServiceStatus.isRunning()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context?.startForegroundService(inte)
                    Log.d(TAG, "Started firebase foreground")

                } else {
                    context?.startService(inte)
                    Log.d(TAG, "Started firebase foreground")
                }
            }
            else {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    Log.d(TAG, "Started set data")
                    val db = FirebaseSetData(user.uid)
                    db.getAttendanceStatus(lect!!, inte)
                }
            }





        }
    }
}









