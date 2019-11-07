package com.attendance.letmeattend.Model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.sql.Time
import javax.security.auth.Subject

data class User(var id : String, var attendance : Int, var location : CollegeLocation, var timetable : TimeTable) {
    
    data class CollegeLocation(var location: Int,var radius: Double );
    
    data class TimeTable(var days : ArrayList<Day>){
       
        data class Day(var subjects : ArrayList<Lecture>){

            data class Lecture(var name : String, var time:CTime, var classLocation:Int, var attendance: Attendance ) {
                data class Attendance(var c_attendance: Int, var t_attendance: Int);
                data class CTime(var s_time: String, var e_time: String);
            }
        }
    }
}