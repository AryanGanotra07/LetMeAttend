package com.attendance.letmeattend.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.attendance.letmeattend.R
import com.attendance.letmeattend.SplashScreen.SplashScreen
import com.attendance.letmeattend.firebase.FirebaseSetData
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class ChangeAttendanceActivity : AppCompatActivity(), DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

    private lateinit var db : FirebaseSetData
    private lateinit var lecture : Lecture
    private val notifBuilder = NotificationBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_attendance)

        val intent = getIntent()
        val bundle = intent.getBundleExtra("lecture")
        lecture = bundle.getParcelable<Lecture>("lecture")

        FirebaseApp.initializeApp(this)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
           db = FirebaseSetData(user.uid)
            val LDialog: AlertDialog = AlertDialog.Builder(this)
                .setTitle(lecture.name)
                .setMessage("Change your attendance")
                .setOnCancelListener(this)
                .setOnDismissListener(this)
                .setCancelable(false)
                .setPositiveButton("Present", onPresentClick)
                .setNegativeButton("Absent", onAbsentClick)
                .setNeutralButton("Class Canceled", onCanceledClick)
                .create()

            LDialog.show()
        }
        else {

            val LDialogNoUser: AlertDialog = AlertDialog.Builder(this)
                .setTitle(lecture.name)
                .setMessage("Some error has occurred. Please login to proceed!")
                .setOnCancelListener(this)
                .setOnDismissListener(this)
                .setCancelable(false)
                .setPositiveButton("Login", onLoginClick)
                .create()

            LDialogNoUser.show()
        }
    }

    private val onPresentClick = object  : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            if (db != null && lecture != null) {
                db.updateAttendance(lecture, 1)
                notifBuilder.removeNotification(lecture.id.hashCode()-1)

            }
        }
    }

    private val onAbsentClick = object  : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            if (db != null && lecture != null) {
                db.updateAttendance(lecture, 0)
                notifBuilder.removeNotification(lecture.id.hashCode()-1)
            }
        }
    }

    private val onCanceledClick = object  : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            notifBuilder.removeNotification(lecture.id.hashCode()-1)
        }
    }

    private val onLoginClick = object  : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            startActivity(Intent(this@ChangeAttendanceActivity, SplashScreen::class.java))
            notifBuilder.removeNotification(lecture.id.hashCode()-1)
            finish()

        }
    }


    override fun onCancel(dialog: DialogInterface?) {
       this@ChangeAttendanceActivity.finish()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        this@ChangeAttendanceActivity.finish()
    }
}
