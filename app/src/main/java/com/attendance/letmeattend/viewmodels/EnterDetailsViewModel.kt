package com.attendance.letmeattend.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MediatorLiveData
import com.attendance.letmeattend.adapters.LectureRecyclerAdapter
import com.attendance.letmeattend.details.listeners.SaveClickListener
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.models.Attendance
import com.attendance.letmeattend.models.CollegeLocation
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.Subject
import com.attendance.letmeattend.services.alarms.MyAlarmManager


class EnterDetailsViewModel : ViewModel() ,  SaveClickListener   {


//    private val user : MediatorLiveData<User> = Repository.getUserLiveData()
      private val repository : Repository = Repository()
      private val attendance : MediatorLiveData<Attendance> = repository.getAttendance()
      private val collegeLocation : MediatorLiveData<CollegeLocation> = repository.getCollegeLocation()
      private val monAdapter : LectureRecyclerAdapter = LectureRecyclerAdapter()
      private val tueAdapter : LectureRecyclerAdapter = LectureRecyclerAdapter()
      private val wedAdapter : LectureRecyclerAdapter = LectureRecyclerAdapter()
      private val thursAdapter : LectureRecyclerAdapter = LectureRecyclerAdapter()
      private val friAdapter : LectureRecyclerAdapter = LectureRecyclerAdapter()
      private val satAdapter : LectureRecyclerAdapter = LectureRecyclerAdapter()





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

    fun getMonLectureRecyclerAdapter() : LectureRecyclerAdapter
    {
        return monAdapter
    }
    fun getTueLectureRecyclerAdapter() : LectureRecyclerAdapter
    {
        return tueAdapter
    }
    fun getWedLectureRecyclerAdapter() : LectureRecyclerAdapter
    {
        return wedAdapter
    }
    fun getThurLectureRecyclerAdapter() : LectureRecyclerAdapter
    {
        return thursAdapter
    } fun getFriLectureRecyclerAdapter() : LectureRecyclerAdapter
    {
        return friAdapter
    } fun getsatLectureRecyclerAdapter() : LectureRecyclerAdapter
    {
        return satAdapter
    }

    fun updateLecture(lecture: Lecture)
    {
        repository.updateLecture(lecture)
    }

    fun getSubjectByName(name : String) : List<Subject>?
    {
        return repository.getSubjectsByName(name)
    }

    fun getSubjects() : MediatorLiveData<ArrayList<Subject>>
    {
        return repository.getSubjects()
    }

    fun deleteLecture(lecture: Lecture)
    {
        repository.deleteLecture(lecture)
    }





    override fun onSave(attendance: Int) {
        setAttendance(Attendance(attendance))
    }


}