package com.attendance.letmeattend.models

data class User(var id : String,
                var attendance : Attendance,
                var location : CollegeLocation,
                var timetable : ArrayList<Day> )