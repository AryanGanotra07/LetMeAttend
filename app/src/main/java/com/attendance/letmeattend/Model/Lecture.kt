package com.attendance.letmeattend.Model



//    data class Lecture(var day : Int, var name : String, var time:CTime, var attendance: LectureAttendance ,var classLocation:Int = 0) {
//
//
//        data class LectureAttendance(var c_attendance: Int = 0, var t_attendance: Int = 0);
//        data class CTime(var s_time: String, var e_time: String);
//    }

class Lecture(var id : String = "" , var day: Int = 0) {

     var name : String = ""
     var s_time: String = ""
     var e_time: String = ""
     var c_attendance: Int = 0
     var t_attendance: Int = 0
     var lat:Double = 0.0
     var lng:Double = 0.0
     var color: Int = 0

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "day" to day,
            "name" to name,
            "s_time" to s_time,
            "e_time" to e_time,
            "c_attendance" to c_attendance,
            "t_attendance" to t_attendance,
            "lat" to lat,
            "lng" to lng,
            "color" to color
        )
    }




}


