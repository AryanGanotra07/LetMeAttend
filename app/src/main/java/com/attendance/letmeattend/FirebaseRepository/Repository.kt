package com.attendance.letmeattend.FirebaseRepository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.attendance.letmeattend.Model.Attendance
import com.attendance.letmeattend.Model.CollegeLocation
import com.attendance.letmeattend.Model.User
import com.google.android.gms.internal.phenotype.zzh.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Repository {

    private val userId : String?  = FirebaseAuth.getInstance()?.currentUser?.uid?.let {
        it
    }

   private val DB_REF_USER = userId?.let {
       FirebaseDatabase.getInstance().reference.child("User").child(
           userId)
   }

    private val DB_REF_COLLEGE_LOC = userId?.let {
        FirebaseDatabase.getInstance().reference.child("User").child(
           userId).child("college_location")
    }

    private val DB_REF_ATTENDANCE =  userId?.let {
        FirebaseDatabase.getInstance().reference.child("User").child(
            userId).child("attendance")
    }


    private val firebaseDatabaseLiveData = FirebaseDatabaseLiveData(DB_REF_USER)
    private val collegeLocationLiveData = FirebaseDatabaseLiveData(DB_REF_COLLEGE_LOC)
    private val collegeAttendanceLiveData = FirebaseDatabaseLiveData(DB_REF_ATTENDANCE)


    private val user : MediatorLiveData<User> = MediatorLiveData()
    private val collegeLocation : MediatorLiveData<CollegeLocation> = MediatorLiveData()
    private val attendance : MediatorLiveData<Attendance> = MediatorLiveData()
    init {
        user.addSource(firebaseDatabaseLiveData, Observer {
            if (it != null) Thread(Runnable { user.postValue(it.getValue(User::class.java))

            }).start()
             else  user.setValue(null)
        })



        collegeLocation.addSource(collegeLocationLiveData, Observer {
            if(it != null)  Thread(Runnable { collegeLocation.postValue(it.getValue(CollegeLocation::class.java)) }).start()
            else collegeLocation.setValue(null)
        })

        attendance.addSource(collegeAttendanceLiveData, Observer {
            if (it != null) Thread(Runnable { attendance.postValue(it.getValue(Attendance :: class.java)) }).start()
        })

    }

    fun getUserLiveData() : MediatorLiveData<User> {
        return user
    }

    fun getCollegeLocation() : MediatorLiveData<CollegeLocation> {
        return collegeLocation
    }
    fun getAttendance() : MediatorLiveData<Attendance> {
        return attendance
    }
    fun setCollegeLocation(location: CollegeLocation)
    {

    }
    fun setAttendance(attendance: Attendance) {

    }
    fun getUserId(): String? {
        return userId
    }
}
