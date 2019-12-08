package com.attendance.letmeattend.firebase

import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.models.Attendance
import com.attendance.letmeattend.models.CollegeLocation
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.Subject
import com.attendance.letmeattend.utils.toast
import com.google.firebase.database.*

class FirebaseSetData(val userId:String) {
    //    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref = userId?.let { database.getReference("User").child(it) }


    fun setAttendance(attendance: Attendance) {
        ref?.child("attendance")?.setValue(attendance)?.addOnCompleteListener {
            if (it.isSuccessful) AppApplication.context?.toast("Attendance Updated")
            else AppApplication.context?.toast("Error in updating attendance")
        }
    }

    fun setCollegeLocation(collegeLocation: CollegeLocation) {
        ref?.child("college_location")?.setValue(collegeLocation)?.addOnCompleteListener {
            if (it.isSuccessful) AppApplication.context?.toast("College Location Updated")
            else AppApplication.context?.toast("Error in updating college location")
        }
    }

    fun addLecture(lecture: Lecture) {
        val key = ref?.child("lectures")?.push()?.key
        if (key != null) {
            lecture.id = key
        }

        ref?.child("lectures").orderByChild("name").equalTo(lecture.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val lectureM: Lecture = p0.getValue(Lecture::class.java)!!
                        lecture.sub_id = lectureM.sub_id
                        addToLectureList(key, lecture)

                    } else {
                        val sub_key = ref?.child("subjects")?.push()?.key
                        if (sub_key != null) {
                           // AppApplication.context?.toast("SUB-KEY-SET")
                           // Log.i("SUB-KEY","SET")
                            lecture.sub_id = sub_key
                        }
                        addToSubjectList(sub_key, lecture)
                        addToLectureList(key, lecture)
                    }

                }

            })
//        ref?.
//            child("lectures")?.
//            setValue(lecture)?.
//            addOnCompleteListener {
//                if (it.isSuccessful) AppApplication.context?.toast("Lecture Added")
//                else AppApplication.context?.toast("Error in adding lecture")
//            }


    }

    private fun addToLectureList(key : String? , lecture: Lecture)
    {
        val childUpdates = HashMap<String, Any>()
        childUpdates["/lectures/$key"] = lecture.toMap()
        ref?.updateChildren(childUpdates)?.addOnCompleteListener {
            if (it.isSuccessful) AppApplication.context?.toast("Lecture Added")
            else AppApplication.context?.toast("Error in adding lecture")
        }
    }

    private fun addToSubjectList(key: String?, lecture: Lecture) {
//            val key = ref?.child("subjects")?.push()?.key
//            if (key != null)
//            {
//                lecture.id = key
//            }

//            }
        val subject: Subject = Subject(
            key!!,
            lecture.name,
            lecture.c_attendance,
            lecture.t_attendance,
            lecture.color
        )
        val childUpdates = HashMap<String, Any>()
        childUpdates["/subjects/$key"] = subject.toMap()
        ref?.updateChildren(childUpdates)?.addOnCompleteListener {
            if (it.isSuccessful) AppApplication.context?.toast("Added to subject")
            else AppApplication.context?.toast("Error in adding subject")
        }



//    fun setUser(attendance: Attendance) {
//        ref?.child("attendance")?.setValue(attendance)?.addOnCompleteListener {
//            if (it.isSuccessful) AppApplication.context?.toast("User Updated")
//            else AppApplication.context?.toast("Error in updating user")
//        }
//    }


    }

    fun updateLecture(lecture: Lecture) {

        ref?.child("lectures")
            .child(lecture.id)
            .setValue(lecture)
            .addOnCompleteListener {
                if (it.isSuccessful) AppApplication.context?.toast("Lecture Updated Successfuly")
                else AppApplication.context?.toast("Lecture Update Not Successful")
            }
        ref?.child("lectures")
            .orderByChild("sub_id")
            .equalTo(lecture.sub_id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                   if (p0.exists())
                   {
                       val lectureM: Lecture = p0.getValue(Lecture::class.java)!!
                       if(!lectureM.name.equals(lecture.name)){

                       }
                   }
                }

            })

    }

    private fun changeSubId(lecture: Lecture)
    {
        ref?.child("lectures")
            .orderByChild("name")
            .equalTo(lecture.name)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                }

            })
    }

}