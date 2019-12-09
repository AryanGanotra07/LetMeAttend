package com.attendance.letmeattend.details.listeners


interface AddSubjectListener {

    fun onAddSubject(day : Int)
    fun onAddSubject(day : Int,subject : com.attendance.letmeattend.models.Subject)
}