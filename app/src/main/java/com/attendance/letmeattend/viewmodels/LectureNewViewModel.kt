package com.attendance.letmeattend.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel

class LectureNewViewModel() : ViewModel() {

     val lectureName : MediatorLiveData<String> = MediatorLiveData()
     val lectureColor : MediatorLiveData<Int> = MediatorLiveData()
     val day  : MediatorLiveData<Int> = MediatorLiveData()
    val startTime = MediatorLiveData<String>()
    val endTime = MediatorLiveData<String>()
     lateinit var lecture : LectureModel

    fun bind(lecture: LectureModel)
    {
//        lectureName.value = lecture.name
//        lectureColor.value = lecture.color
//        day.value = lecture.day
//        startTime.value = lecture.start_time
//        endTime.value = lecture.end_time
        this.lecture = lecture
    }


}