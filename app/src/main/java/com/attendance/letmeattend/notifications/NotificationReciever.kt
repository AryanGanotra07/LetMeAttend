package com.attendance.letmeattend.notifications

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.services.DatabaseService
import com.attendance.letmeattend.utils.toast
import com.google.firebase.auth.FirebaseAuth

class NotificationReciever() : BroadcastReceiver() {
//    val ACTION_YES = "action_yes"
//    val ACTION_NO = "action_no"
//    //val context = AppApplication?.context

    override fun onReceive(context: Context?, intent: Intent?) {
        MyNotificationChannel.createNotifChannel()
        val builder : NotificationBuilder = NotificationBuilder()
      //  val database : FirebaseSetData = FirebaseSetData(FirebaseAuth.getInstance().currentUser!!.uid)
        val dbService = DatabaseService()
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
            dbService.enqueuework(context!!,intent!!)
            builder.removeNotification(intent.getIntExtra("EXTRA_NOTIFICATION_ID",0))

        }
        else if (intent?.action == builder.ACTION_NO)
        {
            context?.toast("NO CLICKED")

            //attended = false
           // database.addAttendance(lect_id,sub_id,attended)
            dbService.enqueuework(context!!,intent!!)
            builder.removeNotification(intent.getIntExtra("EXTRA_NOTIFICATION_ID",0))

        }
        else if (intent?.action == builder.ACTION_NO_CLASS)
        {
            context?.toast("NO CLASS CLICKED")
            builder.removeNotification(intent.getIntExtra("EXTRA_NOTIFICATION_ID",0))
        }

        context?.stopService(inte)






    }
}