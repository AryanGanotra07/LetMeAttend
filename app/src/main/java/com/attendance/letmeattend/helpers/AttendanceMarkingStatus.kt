package com.attendance.letmeattend.helpers

import com.attendance.letmeattend.models.Lecture

object AttendanceMarkingStatus {

    private var attendanceMarking : Boolean = false
    private var lecture : Lecture = Lecture()

    fun getAttendanceMarking() : Boolean {
        return this.attendanceMarking
    }

    fun setAttendanceMarking(marking : Boolean) {
        this.attendanceMarking = marking
    }

    fun setLecture(lecture : Lecture) {
        this.lecture = lecture
    }

    fun getLecture() : Lecture {
        return this.lecture
    }
}