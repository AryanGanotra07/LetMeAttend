package com.attendance.letmeattend.geofencing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = geofencingEvent.errorCode
            context?.toast("")

            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
        geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            // Get the transition details as a String.
            val notifBuilder = NotificationBuilder()
            notifBuilder.buildErrorNotif("IN YOUR COLLEGE",-4)



        } else {
            val notifBuilder = NotificationBuilder()
            notifBuilder.buildErrorNotif("NOT IN YOUR COLLEGE",-4)
        }
    }
}