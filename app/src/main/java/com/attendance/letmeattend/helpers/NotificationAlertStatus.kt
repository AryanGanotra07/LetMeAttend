package com.attendance.letmeattend.helpers

import com.attendance.letmeattend.models.Lecture
import com.crowdfire.cfalertdialog.CFAlertDialog

object NotificationAlertStatus  {

    private var running : Boolean = false
    private var lecture : Lecture = Lecture()
    private lateinit var alertView : CFAlertDialog

    fun isRunning() : Boolean {
        return running
    }

    fun setRunning(running : Boolean) {
        this.running = running
    }
    fun setLecture(lecture : Lecture) {
        this.lecture = lecture
    }

    fun getLecture() : Lecture {
        return this.lecture
    }

    fun setAlertView(alertView : CFAlertDialog) {
        this.alertView = alertView
    }

    fun getAlertView() : CFAlertDialog {
        return alertView
    }

    fun setAbsentMarked() {
        if (this.isRunning()) {
            if (this.getAlertView() != null) {
                val prevAl = NotificationAlertStatus.getAlertView()
                prevAl.setMessage("You have been marked absent as we got no response from you. Tap me to change!!- " + this.lecture.name)
//                   val items = arrayOf("Change")
//                   prevAl.setItems(items, object : DialogInterface.OnClickListener {
//                       override fun onClick(dialog: DialogInterface?, which: Int) {
//                           dialog!!.dismiss()
//                       }
//
//                   })
                prevAl.setTitle("No Response!")
                prevAl.setCancelable(true)
                prevAl.setEnabled(false)
                this.setRunning(false)
            }
        }
    }

    fun setAttendanceMarkedAlready(lecture : Lecture) {
        if (this.isRunning() && this.lecture.id == lecture.id) {
            if (this.getAlertView() != null) {
                val prevAl = NotificationAlertStatus.getAlertView()
                prevAl.setMessage("Your attendance has been marked already -" + this.lecture.name)
//                   val items = arrayOf("Change")
//                   prevAl.setItems(items, object : DialogInterface.OnClickListener {
//                       override fun onClick(dialog: DialogInterface?, which: Int) {
//                           dialog!!.dismiss()
//                       }
//
//                   })
                prevAl.setTitle("Attendance Marked!")
                prevAl.setCancelable(true)
                prevAl.setEnabled(false)
                prevAl.dismiss()
                this.setRunning(false)
            }
        }
    }


}