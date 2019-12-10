package com.attendance.letmeattend.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class LocationService : JobIntentService() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val notifBuilder: NotificationBuilder = NotificationBuilder()
    private val JOB_ID = 12


    fun enqueuework(context: Context, work: Intent) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        context.run { object : Runnable{
            override fun run() {
                context.toast("Called")
            }

        } }
        enqueueWork(context, LocationService::class.java, JOB_ID, work)
    }

    override fun onHandleWork(intent: Intent) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            // this@LocationService.toast("Lattitude:- " + it.latitude + " longitude:- " + it.longitude)
            Log.i("LocationStatus",it.longitude.toString())
            notifBuilder.buildNotification(intent, it)
        }
        fusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.exception != null) {
                //     this.toast("EXCEPTION:" + it.exception!!.message)
                Log.i("LocationStatus", it.exception!!.message.toString())
            }
        }
    }


}