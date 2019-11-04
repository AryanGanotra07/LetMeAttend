package com.attendance.letmeattend.Model

import android.location.Location
import java.sql.Time
import javax.security.auth.Subject

data class User(var id : String, var attendance : Int, var location : CollegeLocation, var timetable : TimeTable) {
    
    data class CollegeLocation(var location: Location,var radius: Float );
    
    data class TimeTable(var days : ArrayList<Day>){
       
        data class Day(var subjects : ArrayList<Lecture>){

            data class Lecture(var name : String, var time:CTime, var classLocation:Location, var attendance: Attendance ) {
                data class Attendance(var c_attendance: Int, var t_attendance: Int);
                data class CTime(var s_time: Time, var e_time: Time);
            }
        }
    }
}