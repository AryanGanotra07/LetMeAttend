package com.attendance.letmeattend.listeners

import com.attendance.letmeattend.models.Lecture

interface OnLectureClickListener {

    fun onLectureClick(lecture: Lecture)
}