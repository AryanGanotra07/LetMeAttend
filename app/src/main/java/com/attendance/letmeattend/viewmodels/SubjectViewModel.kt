package com.attendance.letmeattend.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel

class SubjectViewModel() : ViewModel() {

     val subjectName : MediatorLiveData<String> = MediatorLiveData()
     val subjectColor : MediatorLiveData<Int> = MediatorLiveData()
     val currentAttendance : MediatorLiveData<Int> = MediatorLiveData()
     val totalAttendance : MediatorLiveData<Int> = MediatorLiveData()
     val lectures : MediatorLiveData<List<LectureModel>> = MediatorLiveData()
     lateinit var subject : SubjectModel

    fun bind(subject: SubjectModel)
    {
        subjectName.value = subject.name
        subjectColor.value = subject.color
        currentAttendance.value = subject.current_attendance
        totalAttendance.value = subject.total_attendance
        lectures.value = subject.lectures
        this.subject = subject
    }


}