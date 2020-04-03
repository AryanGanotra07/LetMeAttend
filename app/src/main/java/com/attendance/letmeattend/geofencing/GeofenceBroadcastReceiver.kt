package com.attendance.letmeattend.geofencing

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.sharedpreferences.LocalRepository
import com.attendance.letmeattend.utils.toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "GeofenceBroadcastReceiver"
    override fun onReceive(context: Context?, intent: Intent?) {
        MyNotificationChannel.createAllNotificationChannels()
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = geofencingEvent.errorCode
            context?.toast("error" + errorMessage)
            val notifBuilder = NotificationBuilder()
            notifBuilder.buildErrorNotif("GEOFENCE -ERROR",-4)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ) {

            // Get the transition details as a String.
            val notifBuilder = NotificationBuilder()
            notifBuilder.buildEnterLocationNotification("You have entered your college.","entrying",notifBuilder.ENTRY_EXIT_NOTIF_ID)
            LocalRepository.setGeofenceState(true)
            Log.d(TAG, "Building entry notification ...")

        }
        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
        {
            val notifBuilder = NotificationBuilder()
            notifBuilder.buildEnterLocationNotification("You have exited college.","exiting",notifBuilder.ENTRY_EXIT_NOTIF_ID)
            LocalRepository.setGeofenceState(false)
            Log.d(TAG, "Building exit notification...")

        }
        else {
            val notifBuilder = NotificationBuilder()
            notifBuilder.buildEnterLocationNotification("CAN'T FIND COLLEGE","exiting",notifBuilder.ENTRY_EXIT_NOTIF_ID)
            LocalRepository.setGeofenceState(false)
            Log.d(TAG, "Building no notification...")

        }
    }
}