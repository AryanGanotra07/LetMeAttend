package com.attendance.letmeattend.Model



    data class Lecture(var name : String, var time:CTime, var classLocation:Int, var attendance: LectureAttendance ) {
        data class LectureAttendance(var c_attendance: Int, var t_attendance: Int);
        data class CTime(var s_time: String, var e_time: String);
    }


