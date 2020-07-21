package com.attendance.letmeattend.listeners


import com.attendance.letmeattend.models.LectureModel

interface LectureListeners {

    fun onLectureEdit(position: Int, lecture : LectureModel)
    fun onLectureDelete(lecture: LectureModel)
}