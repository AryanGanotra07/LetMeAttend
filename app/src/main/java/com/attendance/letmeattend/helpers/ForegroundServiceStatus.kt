package com.attendance.letmeattend.helpers

import com.attendance.letmeattend.models.Lecture

object ForegroundServiceStatus {

    private var running : Boolean = false
    private var lecture : Lecture = Lecture()
    private var lastMarkedLecture : Lecture = Lecture()

    fun isRunning() : Boolean {
        return running
    }

    fun setRunning(running : Boolean) {
        ForegroundServiceStatus.running = running
    }

    fun setLecture(lecture : Lecture) {
        ForegroundServiceStatus.lecture = lecture
    }

    fun getLecture() : Lecture {
     return lecture
    }

    fun getLastMarkedLecture() : Lecture {
        return lastMarkedLecture
    }

    fun setLastMarkedLecture(lastMarkedLecture : Lecture) {
        ForegroundServiceStatus.lastMarkedLecture = lastMarkedLecture
    }
}