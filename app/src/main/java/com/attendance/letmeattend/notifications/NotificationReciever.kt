package com.attendance.letmeattend.notifications

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.attendance.letmeattend.helpers.ForegroundServiceStatus
import com.attendance.letmeattend.services.backgroundservices.DatabaseService
import com.attendance.letmeattend.services.foregroundservices.MarkAttendanceForegroundService
import com.attendance.letmeattend.utils.toast


class NotificationReciever() : BroadcastReceiver() {
//    val ACTION_YES = "action_yes"
//    val ACTION_NO = "action_no"
//    //val context = AppApplication?.context
    private val TAG = "NotificationReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        MyNotificationChannel.createAllNotificationChannels()
        val builder : NotificationBuilder = NotificationBuilder()
      //  val database : FirebaseSetData = FirebaseSetData(FirebaseAuth.getInstance().currentUser!!.uid)
        val dbService =
            DatabaseService()
        val inte = intent!!.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)
       // val lectureBundle = intent!!.getBundleExtra("lecture")
       // val lecture = lectureBundle.getParcelable<Lecture>("lecture")
       // val lect_id = lecture.id
        //val sub_id = lecture.sub_id
        //Log.i("LECTURE_ID",lect_id)
        //Log.i("RECIEVED",intent.action)
        //var attended : Boolean = false
        if (intent?.action == builder.ACTION_YES)
        {
            context?.toast("YES CLICKED")

          //  attended = true
          //  dbService.enqueuework(context!!,intent!!)
            //builder.removeNotification(intent.getIntExtra(EXTRA_NOTIFICATION_ID,0))

           // ForegroundServiceStatus.setRunning(false)

        }
        else if (intent?.action == builder.ACTION_NO)
        {
            context?.toast("NO CLICKED")

            //attended = false
           // database.addAttendance(lect_id,sub_id,attended)
          //  dbService.enqueuework(context!!,intent!!)
            //builder.removeNotification(intent.getIntExtra(EXTRA_NOTIFICATION_ID,0))
            //ForegroundServiceStatus.setRunning(false)

        }
        else if (intent?.action == builder.ACTION_NO_CLASS)
        {
            context?.toast("NO CLASS CLICKED")

        }

        val serviceIntent = Intent(context, MarkAttendanceForegroundService::class.java)
        serviceIntent.putExtra("intent", intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(serviceIntent)
            Log.d(TAG, "Started firebase database foreground")

        } else {
            context?.startService(serviceIntent)
            Log.d(TAG, "Started firebase database foreground")
        }

        builder.removeNotification(intent.getIntExtra(EXTRA_NOTIFICATION_ID,0))
        context?.stopService(inte)
        ForegroundServiceStatus.setRunning(false)







    }



}