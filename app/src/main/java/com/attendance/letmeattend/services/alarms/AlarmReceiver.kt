package com.attendance.letmeattend.services.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.sharedpreferences.LocalRepository
import com.attendance.letmeattend.utils.toast
import com.crowdfire.cfalertdialog.CFAlertDialog
import java.util.*


class AlarmReceiver() : BroadcastReceiver() {


    private val TAG = "AlarmReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {

        val state = LocalRepository.getGeofenceState()
        val day = intent?.getIntExtra("day", 1)
        val lectureBundle = intent?.getBundleExtra("lecture")
        val lect = lectureBundle?.getParcelable<Lecture>("lecture")
        val hour = intent?.getIntExtra("hour", 0)
        val min = intent?.getIntExtra("min", 0)
        Log.i("DETAILS-DAY", day.toString() + lect?.name)
        Log.i("DETAILS-HOUR", hour.toString())
        val calendar = Calendar.getInstance()
        Log.i("DETAILS-DAY-CURRENT", calendar.get(Calendar.DAY_OF_WEEK).toString())
        Log.i("DETAILS-HOUR-CURRENT", calendar.get(Calendar.HOUR_OF_DAY).toString())
        Log.d(TAG, state.toString())

            if (hour == calendar.get(Calendar.HOUR_OF_DAY)
                && day == calendar.get(Calendar.DAY_OF_WEEK)
                && min == calendar.get(Calendar.MINUTE)
            ) {

                    Repository.getAttendanceStatus(lect!!, intent)


                }




    }
}









