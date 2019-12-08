package com.attendance.letmeattend.models

data class Subject(val id : String, val name : String, val c_attendance : Int , val t_attendance : Int, val color : Int)
{
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "c_attendance" to c_attendance,
            "t_attendance" to t_attendance,
            "color" to color
        )
    }

    override fun toString(): String {
        return name
    }

}


