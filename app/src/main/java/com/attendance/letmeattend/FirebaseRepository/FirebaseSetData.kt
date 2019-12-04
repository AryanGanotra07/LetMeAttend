package com.attendance.letmeattend.FirebaseRepository

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.attendance.letmeattend.Application.AppApplication
import com.attendance.letmeattend.Model.Attendance
import com.attendance.letmeattend.Utils.toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class FirebaseSetData(){
    private val database : FirebaseDatabase  = FirebaseDatabase.getInstance()
    private val ref = Repository.getUserId()?.let { database.getReference("User").child(it) }




    fun setAttendance(attendance: Attendance) {
        ref?.child("attendance")?.setValue(attendance)?.addOnCompleteListener {
            if (it.isSuccessful) AppApplication.context?.toast("Attendance Updated")
            else AppApplication.context?.toast("Error in updating attendance")


        }
    }




}