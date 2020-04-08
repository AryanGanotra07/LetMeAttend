package com.attendance.letmeattend.services.foregroundservices

import com.attendance.letmeattend.models.Lecture

object ForegroundServiceStatus {

    private var running : Boolean = false
    private var lecture : Lecture = Lecture()
    private var lastMarkedLecture : Lecture = Lecture()

    fun isRunning() : Boolean {
        return running
    }

    fun setRunning(running : Boolean) {
        this.running = running
    }

    fun setLecture(lecture : Lecture) {
        this.lecture = lecture
    }

    fun getLecture() : Lecture {
     return this.lecture
    }

    fun getLastMarkedLecture() : Lecture {
        return this.lastMarkedLecture
    }

    fun setLastMarkedLecture(lastMarkedLecture : Lecture) {
        this.lastMarkedLecture = lastMarkedLecture
    }
}