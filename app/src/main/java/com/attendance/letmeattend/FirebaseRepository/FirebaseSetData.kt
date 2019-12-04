package com.attendance.letmeattend.FirebaseRepository

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.attendance.letmeattend.Application.AppApplication
import com.attendance.letmeattend.Model.Attendance
import com.attendance.letmeattend.Model.CollegeLocation
import com.attendance.letmeattend.Utils.toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

class FirebaseSetData(){
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val database : FirebaseDatabase  = FirebaseDatabase.getInstance()
    private val ref = userId?.let { database.getReference("User").child(it) }




    fun setAttendance(attendance: Attendance) {
        ref?.child("attendance")?.setValue(attendance)?.addOnCompleteListener {
            if (it.isSuccessful) AppApplication.context?.toast("Attendance Updated")
            else AppApplication.context?.toast("Error in updating attendance")
        }
    }

    fun setCollegeLocation(collegeLocation: CollegeLocation) {
        ref?.child("college_location")?.setValue(collegeLocation)?.addOnCompleteListener {
            if (it.isSuccessful) AppApplication.context?.toast("College Location Updated")
            else AppApplication.context?.toast("Error in updating college location")
        }
    }

//    fun setUser(attendance: Attendance) {
//        ref?.child("attendance")?.setValue(attendance)?.addOnCompleteListener {
//            if (it.isSuccessful) AppApplication.context?.toast("User Updated")
//            else AppApplication.context?.toast("Error in updating user")
//        }
//    }






}