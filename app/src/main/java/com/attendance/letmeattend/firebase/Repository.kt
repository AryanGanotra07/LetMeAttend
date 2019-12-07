package com.attendance.letmeattend.firebase

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.attendance.letmeattend.models.Attendance
import com.attendance.letmeattend.models.CollegeLocation
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Repository() {

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

    private val DB_REF_LECTURES =  userId?.let {
        FirebaseDatabase.getInstance().reference.child("User").child(
            userId).child("lectures")
    }


    private val firebaseDatabaseLiveData = FirebaseDatabaseLiveData(DB_REF_USER)
    private val collegeLocationLiveData = FirebaseDatabaseLiveData(DB_REF_COLLEGE_LOC)
    private val collegeAttendanceLiveData = FirebaseDatabaseLiveData(DB_REF_ATTENDANCE)
    private val lecturesLiveData = FirebaseDatabaseLiveData(DB_REF_LECTURES)
    private val database : FirebaseSetData = FirebaseSetData(userId!!)


    private val user : MediatorLiveData<User> = MediatorLiveData()
    private val collegeLocation : MediatorLiveData<CollegeLocation> = MediatorLiveData()
    private val attendance : MediatorLiveData<Attendance> = MediatorLiveData()
    private val lectures : MediatorLiveData<ArrayList<Lecture>> = MediatorLiveData()
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

        lectures.addSource(lecturesLiveData, Observer {
            if (it!=null && it.hasChildren()) {
                val lecturesList: ArrayList<Lecture> = ArrayList()
                for (lecture in it.children)
                {
                    val lect : Lecture = lecture.getValue(Lecture::class.java)!!
                    lecturesList.add(lect)
                }
                Thread(Runnable { lectures.postValue(lecturesList) }).start()
            }
            else lectures.value = null
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
        database.setCollegeLocation(location)
    }
    fun setAttendance(attendance: Attendance) {
        database.setAttendance(attendance)
    }
    fun getUserId(): String? {
        return userId
    }
    fun addLecture(lecture : Lecture)
    {
        database.addLecture(lecture)
    }

    fun getLectures() : MediatorLiveData<ArrayList<Lecture>>
    {
        return lectures
    }

    fun updateLecture(lecture : Lecture)
    {
        database.updateLecture(lecture)
    }
}
