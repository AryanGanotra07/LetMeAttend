package com.attendance.letmeattend.listeners


interface AddSubjectListener {

    fun onAddSubject(day : Int)
    fun onAddSubject(day : Int,subject : com.attendance.letmeattend.models.Subject)
}