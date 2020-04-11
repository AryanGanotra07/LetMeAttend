package com.attendance.letmeattend.helpers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.attendance.letmeattend.R
import com.attendance.letmeattend.activities.ChangeAttendanceActivity
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.models.Lecture
import com.crowdfire.cfalertdialog.CFAlertDialog

object NotificationAlertStatus  {

    private val TAG = "NotificationAlertStatus"

    private var running : Boolean = false
    private var lecture : Lecture = Lecture()
    private var prevLecture : Lecture = Lecture()
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

    fun setPrevLecture(lecture : Lecture) {
        this.prevLecture = lecture
    }

    fun setAbsentMarked(nLecture: Lecture, nAlertView: CFAlertDialog ) {
        Log.d(TAG, "Prev lecture name is "+ lecture.name)
        if (this.isRunning()) {
            if (this.getAlertView() != null) {
                val prevAl = this.getAlertView()
                prevAl.dismiss()
                prevAl.cancel()
                prevAl.setEnabled(false)
                prevAl.hide()
                Repository.addAttendance(lecture, false)
                this.setRunning(false)

//
//                prevAl.setMessage("You have been marked absent as we got no response from you. Tap me to change!!- " + this.lecture.name)
////                   val items = arrayOf("Change")
////                   prevAl.setItems(items, object : DialogInterface.OnClickListener {
////                       override fun onClick(dialog: DialogInterface?, which: Int) {
////                           dialog!!.dismiss()
////                       }
////
////                   })
//
//                prevAl.setTitle("No Response!")
//                prevAl.setCancelable(true)
////                prevAl.setEnabled(false)
//
//                this.setPrevLecture(lecture)
//                prevAl.setContentView(R.layout.my_alert_dialog)
//                val change = prevAl.findViewById<Button>(R.id.change)
//
////                val present = prevAl.findViewById<Button>(R.id.present)
////                val absent = prevAl.findViewById<Button>(R.id.absent)
//
////                present!!.setOnClickListener {
////                    Repository.updateAttendance(this.prevLecture, true)
////                    prevAl.dismiss()
////                }
////                absent!!.setOnClickListener {
////                    Repository.updateAttendance(this.prevLecture, false)
////                    prevAl.dismiss()
////                }
//                change!!.setOnClickListener {
//                    val intent = Intent(prevAl.context, ChangeAttendanceActivity::class.java)
//                    val bundle = Bundle()
//                    bundle.putParcelable("lecture",this.prevLecture)
//                    intent.putExtra("lecture",bundle)
//                    prevAl.dismiss()
//                    prevAl.cancel()
//                    prevAl.hide()
//                    prevAl.context.startActivity(intent)
//                }
//                //this.setRunning(false)
//                //Repository.addAttendance(lecture, false)
            }
        }
        this.setLecture(nLecture)
        this.setAlertView(nAlertView)
        this.setRunning(true)

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