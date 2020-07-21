package com.attendance.letmeattend.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.JsonObject
import org.json.JSONObject

data class LectureModel(var id : Int, var day : Int, var start_time : String, var end_time : String,var name: String = "Maths", var color : Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(day)
        parcel.writeString(start_time)
        parcel.writeString(end_time)
        parcel.writeString(name)
        parcel.writeInt(color)
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "day" to day,
            "name" to name,
            "start_time" to start_time,
            "end_time" to end_time,
            "color" to color
        )
    }

    fun toJSON() : JsonObject {
        val jsonObject = JsonObject()
        jsonObject.addProperty("day", day)
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("color", color)
        jsonObject.addProperty("start_time", start_time)
        jsonObject.addProperty("end_time", end_time)
        return jsonObject
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LectureModel> {
        override fun createFromParcel(parcel: Parcel): LectureModel {
            return LectureModel(parcel)
        }

        override fun newArray(size: Int): Array<LectureModel?> {
            return arrayOfNulls(size)
        }
    }


}