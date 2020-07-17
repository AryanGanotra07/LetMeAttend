package com.attendance.letmeattend.models

import android.os.Parcel
import android.os.Parcelable

data class LectureModel(var id : Int, var day : Int, var start_time : String, var end_time : String,var name: String = "Maths", var color : String = "#000000") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(day)
        parcel.writeString(start_time)
        parcel.writeString(end_time)
        parcel.writeString(name)
        parcel.writeString(color)
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