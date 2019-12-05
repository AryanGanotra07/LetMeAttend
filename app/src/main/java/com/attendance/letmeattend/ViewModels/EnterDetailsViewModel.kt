package com.attendance.letmeattend.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.MediatorLiveData
import com.attendance.letmeattend.FirebaseRepository.Repository
import com.attendance.letmeattend.Model.Attendance
import com.attendance.letmeattend.Model.CollegeLocation
import com.attendance.letmeattend.Model.Lecture
import com.attendance.letmeattend.Model.User
import com.google.firebase.auth.FirebaseAuth


class EnterDetailsViewModel : ViewModel() {


//    private val user : MediatorLiveData<User> = Repository.getUserLiveData()
      private val repository : Repository = Repository()
      private val attendance : MediatorLiveData<Attendance> = repository.getAttendance()
      private val collegeLocation : MediatorLiveData<CollegeLocation> = repository.getCollegeLocation()


    fun getCollegeLocation(): MediatorLiveData<CollegeLocation> {
        return collegeLocation
    }

    fun getAttendance() : MediatorLiveData<Attendance>
    {
        return attendance
    }

    fun setAttendance(attendance: Attendance)
    {
        repository.setAttendance(attendance)
    }
    fun setCollegeLocation(collegeLocation: CollegeLocation)
    {
        repository.setCollegeLocation(collegeLocation)
    }
    fun addLecture(lecture : Lecture)
    {
        repository.addLecture(lecture)
    }

    fun getLectures() : MediatorLiveData<ArrayList<Lecture>>
    {
        return repository.getLectures()
    }



}