package com.attendance.letmeattend.services.boot

import android.app.Notification
import android.app.NotificationChannel
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.models.CollegeLocation
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BootCompleteService() : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val context = AppApplication?.context
        context?.toast("Starting servicee")
        FirebaseApp.initializeApp(context!!)
        MyNotificationChannel.createNotifChannel()
        val notifBuilder = NotificationBuilder()
        var notif= notifBuilder.buildErrorNotif("Started service",-10)
        Log.i("Boot","ID not found")
        if (FirebaseAuth.getInstance().currentUser!=null)
        {
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            if (uid !=null)
            {
                FirebaseDatabase.getInstance()
                    .reference.child("User")
                    .child(uid).child("college_location")
                    .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        notif = notifBuilder.buildErrorNotif("ERROR HAI BHAI",-4)
                        startForeground(10,notif)
                    }

                    override fun onDataChange(p0: DataSnapshot) {

                        notif  = notifBuilder.buildErrorNotif(p0.toString(),-4)
//                        if (location!=null && location.lat!=null)
//                        {
//
//                        }
//                        else
//                        {
//                            notif = notifBuilder.buildErrorNotif("ERROR HAI BHAI",-4)
//                        }
                        startForeground(10,notif)
                    }

                })



            }
            else
            {
                MyNotificationChannel.createNotifChannel()
                val notifBuilder = NotificationBuilder()
                notif= notifBuilder.buildErrorNotif("ID not found",-3)
                startForeground(10,notif)
                Log.i("Boot","ID not found")


            }
        }
        else
        {
            MyNotificationChannel.createNotifChannel()
            val notifBuilder = NotificationBuilder()
            notif= notifBuilder.buildErrorNotif("User not found",-10)
            Log.i("Boot","ID not found")
        }
        startForeground(10,notif)

        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}