package com.attendance.letmeattend.models

import android.os.Parcel
import android.os.Parcelable


//    data class Lecture(var day : Int, var name : String, var time:CTime, var attendance: LectureAttendance ,var classLocation:Int = 0) {
//
//
//        data class LectureAttendance(var c_attendance: Int = 0, var t_attendance: Int = 0);
//        data class CTime(var s_time: String, var e_time: String);
//    }

class Lecture(var id : String = "" , var day: Int = 0) : Parcelable {

     var name : String = ""
     var s_time: String = ""
     var e_time: String = ""
     var c_attendance: Int = 0
     var t_attendance: Int = 0
     var lat:Double = 0.0
     var lng:Double = 0.0
     var color: Int = 0
     var sub_id = ""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
        name = parcel.readString()
        s_time = parcel.readString()
        e_time = parcel.readString()
        c_attendance = parcel.readInt()
        t_attendance = parcel.readInt()
        lat = parcel.readDouble()
        lng = parcel.readDouble()
        color = parcel.readInt()
        sub_id = parcel.readString()
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "sub_id" to sub_id,
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeInt(day)
        parcel.writeString(name)
        parcel.writeString(s_time)
        parcel.writeString(e_time)
        parcel.writeInt(c_attendance)
        parcel.writeInt(t_attendance)
        parcel.writeDouble(lat)
        parcel.writeDouble(lng)
        parcel.writeInt(color)
        parcel.writeString(sub_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Lecture> {
        override fun createFromParcel(parcel: Parcel): Lecture {
            return Lecture(parcel)
        }

        override fun newArray(size: Int): Array<Lecture?> {
            return arrayOfNulls(size)
        }
    }


}


