package com.attendance.letmeattend.firebase

import android.app.AlarmManager
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.models.*
import com.attendance.letmeattend.services.alarms.MyAlarmManager
import com.attendance.letmeattend.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

object Repository {

    private val TAG = "Repository"

    private val userId : String?  = FirebaseAuth.getInstance()?.currentUser?.uid?.let {
        it
    }

   private val DB_REF_USER = userId?.let {
       FirebaseDatabase.getInstance().reference.child("User").child(
           userId)
   }

    private val DB_REF_COLLEGE_LOC = userId?.let {
       DB_REF_USER?.child("college_location")
    }

    private val DB_REF_ATTENDANCE =  userId?.let {
        DB_REF_USER?.child("attendance")
    }

    private val DB_REF_LECTURES =  userId?.let {
        DB_REF_USER?.child("lectures")
    }

    private val DB_REF_SUBJECTS = userId?.let {
        DB_REF_USER?.child("subjects")
    }

    private val DB_REF_ATTENDANCE_STATUS = userId?.let {
        DB_REF_USER?.child("attendanceStatus")
    }

//    private val DB_REF_LAT = userId?.let {
//        DB_REF_LECTURES?.child("lat")
//    }
//
//    private val DB_REF_LNG = userId?.let {
//        DB_REF_LECTURES?.child("lng")
//    }


    private val firebaseDatabaseLiveData = FirebaseDatabaseLiveData(DB_REF_USER)
    private val collegeLocationLiveData = FirebaseDatabaseLiveData(DB_REF_COLLEGE_LOC)
    private val collegeAttendanceLiveData = FirebaseDatabaseLiveData(DB_REF_ATTENDANCE)
    private val lecturesLiveData = FirebaseDatabaseLiveData(DB_REF_LECTURES)
    private val subjectsLiveData = FirebaseDatabaseLiveData(DB_REF_SUBJECTS)
    private val attendanceStatusLiveData = FirebaseDatabaseLiveData(DB_REF_ATTENDANCE_STATUS)
    private val database : FirebaseSetData = FirebaseSetData(userId!!)

    private val user : MediatorLiveData<User> = MediatorLiveData()
    private val collegeLocation : MediatorLiveData<CollegeLocation> = MediatorLiveData()
    private val attendance : MediatorLiveData<Attendance> = MediatorLiveData()
    private val lectures : MediatorLiveData<ArrayList<Lecture>> = MediatorLiveData()
    private val subjects : MediatorLiveData<ArrayList<Subject>> = MediatorLiveData()
    private val attendanceStatus : MediatorLiveData<ArrayList<AttendanceStatus>> = MediatorLiveData()

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
            if (it != null) Thread(Runnable {
                attendance.postValue(it.getValue(Attendance :: class.java))
                //Log.d(TAG, it.getValue(Attendance :: class.java)?.attendance.toString())
            }).start()
        })

        attendanceStatus.addSource(attendanceStatusLiveData, Observer {
            if (it != null && it.hasChildren()) {
                val attendanceStatusList : ArrayList<AttendanceStatus> = ArrayList()
                for (attendanceStatus in it.children) {
                    val attendanceStat : AttendanceStatus = attendanceStatus.getValue(AttendanceStatus::class.java)!!
                    attendanceStatusList.add(attendanceStat)
                }
                Thread(Runnable { attendanceStatus.postValue(attendanceStatusList) }).start()
            }
            else {
                attendanceStatus.value = null
            }
        })

        lectures.addSource(lecturesLiveData, Observer {
            if (it!=null && it.hasChildren()) {

                val lecturesList: ArrayList<Lecture> = ArrayList()
                for (lecture in it.children)
                {
                    val lect : Lecture = lecture.getValue(Lecture::class.java)!!
                    lecturesList.add(lect)
                }
                MyAlarmManager.setAllAlarms(lecturesList)
                Thread(Runnable { lectures.postValue(lecturesList) }).start()
            }
            else lectures.value = null
        })
        subjects.addSource(subjectsLiveData, Observer {
            if (it!=null && it.hasChildren())
            {
                val subjectList: ArrayList<Subject> = ArrayList()
                for (subject in it.children)
                {
                    val subj : Subject = subject.getValue(Subject :: class.java)!!
                    subjectList.add(subj)

                }
                Thread(Runnable { subjects.postValue(subjectList) }).start()
            }
            else{

                subjects.value = null

            }
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

    fun getSubjectsByName(name : String) : List<Subject>?
    {
       return subjects.value?.filter { value -> value.name.toLowerCase().contains(name.toLowerCase()) }
    }

    fun getSubjects() : MediatorLiveData<ArrayList<Subject>>
    {
        return subjects
    }

    fun deleteLecture(lecture: Lecture)
    {
        database.deleteLecture(lecture)
    }

   fun getAttendanceStatus(lecture: Lecture) : Date{
       database.getAttendanceStatus(lecture)
       return Calendar.getInstance().time
   }

//    fun addAttendance(id : String,sub_id :String, attended : Boolean)
//    {
//        database.addAttendance(id, sub_id,attended)
//    }
}
