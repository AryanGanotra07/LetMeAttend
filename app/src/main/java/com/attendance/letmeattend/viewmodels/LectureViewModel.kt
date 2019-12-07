package com.attendance.letmeattend.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.attendance.letmeattend.models.Lecture

class LectureViewModel() : ViewModel() {

    private val lectureName : MediatorLiveData<String> = MediatorLiveData()
    private val lectureSTime : MediatorLiveData<String> = MediatorLiveData()
    private val lectureETime  : MediatorLiveData<String> = MediatorLiveData()
    private val lectureColor : MediatorLiveData<Int> = MediatorLiveData()

    fun bind(lecture: Lecture)
    {
        lectureName.value = lecture.name
        lectureSTime.value = lecture.s_time
        lectureETime.value = lecture.e_time
        lectureColor.value = lecture.color
    }

    fun getLectureName() : MediatorLiveData<String>
    {
        return lectureName
    }
    fun getLectureSTime() : MediatorLiveData<String>
    {
        return lectureSTime
    }
    fun getLectureETime() : MediatorLiveData<String>
    {
        return lectureETime
    }

    fun getColor(): MediatorLiveData<Int>
    {
        return lectureColor
    }


}