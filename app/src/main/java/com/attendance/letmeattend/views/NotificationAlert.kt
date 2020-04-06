package com.attendance.letmeattend.views

import android.content.Context
import android.content.DialogInterface
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.utils.toast
import com.crowdfire.cfalertdialog.CFAlertDialog

class NotificationAlert(val context : Context) {


    fun executeDialog(lecture : Lecture?) {
        val builder :  CFAlertDialog.Builder  = CFAlertDialog.Builder(context)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
            .setTitle("Time for attendance!!")
            .setMessage("Please mark your attendance for "+ lecture?.name + " from " + lecture?.s_time + " to " + lecture?.e_time )
            .addButton("Present", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    context?.toast("Clicked");
                    Repository.addAttendance(lecture!!, true)
                    dialog?.dismiss()
                }
            })
            .addButton("Absent", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED,object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    context?.toast("Clicked");
                    Repository.addAttendance(lecture!!, false)
                    dialog?.dismiss()

                }
            })
            .setCancelable(false)


        builder.show();
    }
}