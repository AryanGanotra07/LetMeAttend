package com.attendance.letmeattend.services.backgroundservices

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class LocationService : JobIntentService() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

   // private val JOB_ID = 12


    fun enqueuework(context: Context, work: Intent) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        context.run { object : Runnable{
            override fun run() {
                context.toast("Called")
            }

        } }
        val JOB_ID = work.getIntExtra("id",0)
        enqueueWork(context, LocationService::class.java, JOB_ID, work)
    }

    override fun onHandleWork(intent: Intent) {
        MyNotificationChannel.createAllNotificationChannels()
        val notifBuilder: NotificationBuilder = NotificationBuilder()
        val notif = notifBuilder.buildErrorNotif("Running Location Service",2)
        startForeground(123,notif)
        val lectureBundle = intent.getBundleExtra("lecture")
        val lecture = lectureBundle.getParcelable<Lecture>("lecture")
        val id = intent.getIntExtra("id",0)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            // this@LocationService.toast("Lattitude:- " + it.latitude + " longitude:- " + it.longitude)
//            Log.i("LocationStatusID",intent.getStringExtra("id"))

            if (lecture.lat!=0.0 || lecture.lng != 0.0)
            {
                if (it.latitude<lecture.lat+1 && it.latitude>lecture.lat-1
                    && it.longitude<lecture.lng+1 && it.longitude>lecture.lng-1)
                {
                    //mark attendance. ask user to recheck attendance
                }
                else
                {
                    //ask user if he is attending. If yes
                    // update location
                    // and mark present. If no mark absent. If no class,then ignore
                }
            }
            else
            {
                //ask user if he is attending lecture. if yes then save location , if no mark absent, if no class then ignore

            }
            notifBuilder.buildNotification(intent, it)

        }
        fusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.exception != null) {
                //     this.toast("EXCEPTION:" + it.exception!!.message)
                Log.i("LocationStatus", it.exception!!.message.toString())
                notifBuilder.buildErrorNotif("Can't find location"+it.exception?.message.toString(),id)
            }
        }
    }


}