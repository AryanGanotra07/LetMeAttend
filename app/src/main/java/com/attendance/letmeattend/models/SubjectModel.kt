package com.attendance.letmeattend.models

import android.os.Parcel
import android.os.Parcelable

data class SubjectModel(var id : Int, var current_attendance : Int, var total_attendance : Int, var color : String, var name : String, var lectures : List<LectureModel> ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(LectureModel)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(current_attendance)
        parcel.writeInt(total_attendance)
        parcel.writeString(color)
        parcel.writeString(name)
        parcel.writeTypedList(lectures)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubjectModel> {
        override fun createFromParcel(parcel: Parcel): SubjectModel {
            return SubjectModel(parcel)
        }

        override fun newArray(size: Int): Array<SubjectModel?> {
            return arrayOfNulls(size)
        }
    }
}