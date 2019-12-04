package com.attendance.letmeattend.Model

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.sql.Time
import javax.security.auth.Subject

data class User(var id : String,
                var attendance : Attendance,
                var location : CollegeLocation,
                var timetable : ArrayList<Day> )