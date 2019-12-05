package com.attendance.letmeattend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MediatorLiveData
import com.attendance.letmeattend.details.listeners.AddSubjectListener
import com.attendance.letmeattend.details.listeners.SaveClickListener
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.models.Attendance
import com.attendance.letmeattend.models.CollegeLocation
import com.attendance.letmeattend.models.Lecture


class EnterDetailsViewModel : ViewModel() ,  SaveClickListener   {


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


    override fun onSave(attendance: Int) {
        setAttendance(Attendance(attendance))
    }


}