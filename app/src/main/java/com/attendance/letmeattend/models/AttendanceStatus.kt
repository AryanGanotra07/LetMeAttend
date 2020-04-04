package com.attendance.letmeattend.models

import java.sql.Time
import java.sql.Timestamp
import java.util.*

data class AttendanceStatus (var id : String = "",
                             var lect_id : String = "",
                             var sub_id : String = "",
                             var attended : Boolean = false,
                             var s_time : String = "",
                             var e_time : String = "",
                             var day : Int = 0,
                             var last_marked : Date = Calendar.getInstance().time
                             ) {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "sub_id" to sub_id,
            "lect_id" to lect_id,
            "attended" to attended,
            "s_time" to s_time,
            "e_time" to e_time,
            "day" to day,
            "last_marked" to last_marked
        )
    }
}