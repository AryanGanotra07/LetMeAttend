package com.attendance.letmeattend.services.foregroundservices

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.helpers.ForegroundServiceStatus
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.utils.toast
import com.google.android.gms.location.LocationServices

class MyForegroundService() : Service(){
    private val TAG = "MyForegroundService"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        MyNotificationChannel.createAllNotificationChannels()
        val notifBuilder: NotificationBuilder = NotificationBuilder()
        var notif = notifBuilder.buildErrorNotif("Running Location Service",2)
        val lectureBundle = intent!!.getBundleExtra("lecture")
        val lecture = lectureBundle.getParcelable<Lecture>("lecture")
        val id = intent.getIntExtra("id",0)
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
           notif = notifBuilder.buildNotification(intent!!, it)

            //startForeground(lecture.id.hashCode(),notif)

        }
        fusedLocationClient.lastLocation.addOnCompleteListener {
            if (it.exception != null) {
                //     this.toast("EXCEPTION:" + it.exception!!.message)
                Log.i("LocationStatus", it.exception!!.message.toString())
                notifBuilder.buildErrorNotif("Can't find location"+it.exception?.message.toString(),id)
            }
        }

        Log.d(TAG, ForegroundServiceStatus.isRunning().toString())


        startForeground(lecture.id.hashCode(),notif)
        if (ForegroundServiceStatus.isRunning()) {
            if (lecture.id != ForegroundServiceStatus.getLecture().id) {
                notifBuilder.buildNoResponseNotification(ForegroundServiceStatus.getLecture())
                Log.d(TAG, "Building notification" + ForegroundServiceStatus.getLecture().name)
                ForegroundServiceStatus.setRunning(false)
            }
        }
        ForegroundServiceStatus.setRunning(true)
        Log.d(TAG,"Building foreground"+lecture.name)
        ForegroundServiceStatus.setLecture(lecture)
//        val broadcastIntent = Intent("NEW_NOTIFICATION")
//        broadcastIntent.putExtra("intent",intent)
//        broadcastIntent.putExtra("lecture", lectureBundle)
//        broadcastIntent.setPackage("com.attendance.letmeattend")
//        context?.sendBroadcast(broadcastIntent);
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MyForegroundService created")
    }




    override fun onDestroy() {
        stopForeground(Service.STOP_FOREGROUND_REMOVE)
        AppApplication?.context?.toast("Previous notification destroyed")
        Log.d(TAG, "MyForegroundService ended")
        super.onDestroy()
    }


}