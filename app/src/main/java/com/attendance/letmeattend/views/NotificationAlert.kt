package com.attendance.letmeattend.views

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.helpers.NotificationAlertStatus
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.services.foregroundservices.ForegroundServiceStatus
import com.attendance.letmeattend.utils.toast
import com.crowdfire.cfalertdialog.CFAlertDialog

class NotificationAlert(val context : Context) {


    fun executeDialog(lecture : Lecture?, intent : Intent) : CFAlertDialog {
        val builder :  CFAlertDialog.Builder  = CFAlertDialog.Builder(context)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
            .setTitle("Time for attendance!!")
            .setMessage("Please mark your attendance for "+ lecture?.name + " from " + lecture?.s_time + " to " + lecture?.e_time )
            .addButton("Present", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    context?.toast("Clicked");
                    Repository.addAttendance(lecture!!, true)
                    val notifBuilder = NotificationBuilder()
                    val id = intent.getIntExtra("intid",0)
                    context?.stopService(intent)
                    notifBuilder.removeNotification(id)
                    ForegroundServiceStatus.setRunning(false)
                    NotificationAlertStatus.setRunning(false)
                    dialog?.dismiss()
                }
            })
            .addButton("Absent", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    context?.toast("Clicked");
                    Repository.addAttendance(lecture!!, false)
                    val notifBuilder = NotificationBuilder()
                    val id = intent.getIntExtra("intid",0)
                    context?.stopService(intent)
                    notifBuilder.removeNotification(id)
                    ForegroundServiceStatus.setRunning(false)
                    NotificationAlertStatus.setRunning(false)
                    dialog?.dismiss()

                }
            })
            .setCancelable(false)

        val al = builder.show();
        return al
    }

    private fun setNotificationAlertStatus() {
        NotificationAlertStatus.setRunning(false)
    }
}