package com.attendance.letmeattend.firebase

import android.location.Location
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.models.Attendance
import com.attendance.letmeattend.models.CollegeLocation
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.Subject
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.services.alarms.MyAlarmManager
import com.attendance.letmeattend.utils.toast
import com.google.firebase.database.*

class FirebaseSetData(val userId: String) {
    //    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val notifBuilder = NotificationBuilder()
    private val ref = userId?.let {
        database.getReference("User").child(it)
    }


  init {
      MyNotificationChannel.createAllNotificationChannels()
  }


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
                        val child = p0.children.first()
                        Log.i("CHILD", child.toString())
                        val lectureM: Lecture = child.getValue(Lecture::class.java)!!
                        lecture.sub_id = lectureM.sub_id
                        Log.i("SUB-KEY", lectureM.name + lectureM.sub_id)
                        addToLectureList(key, lecture)

                    } else {
                        val sub_key = ref?.child("subjects")?.push()?.key
                        if (sub_key != null) {
                            // AppApplication.context?.toast("SUB-KEY-SET")
                            Log.i("SUB-KEY", "SET")
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

    private fun addToLectureList(key: String?, lecture: Lecture) {
        val childUpdates = HashMap<String, Any>()
        childUpdates["/lectures/$key"] = lecture.toMap()
        ref?.updateChildren(childUpdates)?.addOnCompleteListener {
            if (it.isSuccessful) {


                AppApplication.context?.toast("Lecture Added")
            } else AppApplication.context?.toast("Error in adding lecture")
        }
        ref
            .child("subjects")
            .child(lecture.sub_id)
            .child("lect_ids")
            .child(key!!).setValue(key)
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
            if (it.isSuccessful) {
                AppApplication.context?.toast("Added to subject")
            } else AppApplication.context?.toast("Error in adding subject")
        }


//    fun setUser(attendance: Attendance) {
//        ref?.child("attendance")?.setValue(attendance)?.addOnCompleteListener {
//            if (it.isSuccessful) AppApplication.context?.toast("User Updated")
//            else AppApplication.context?.toast("Error in updating user")
//        }
//    }


    }

    fun updateLecture(lecture: Lecture) {

//
//        ref?.child("lectures")
//            .orderByChild("sub_id")
//            .equalTo(lecture.sub_id)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//
//                override fun onDataChange(p0: DataSnapshot) {
//                   if (p0.exists())
//                   {
//                       val lectureM: Lecture = p0.getValue(Lecture::class.java)!!
//                       if(!lectureM.name.equals(lecture.name)){
//                           changeSubId(lecture)
//                       }
//                       else
//                       {
//                           updateLectureFinal(lecture)
//                       }
//                   }
//                }
//
//            })

        updateLectureFinal(lecture)

    }

    private fun updateLectureFinal(lecture: Lecture) {
        ref?.child("lectures")
            .child(lecture.id)
            .setValue(lecture)
            .addOnCompleteListener {
                if (it.isSuccessful) AppApplication.context?.toast("Lecture Updated Successfuly")
                else AppApplication.context?.toast("Lecture Update Not Successful")
            }
    }

    private fun changeSubId(lecture: Lecture) {
        ref?.child("lectures")
            .orderByChild("name")
            .equalTo(lecture.name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        val lectureM: Lecture = p0.getValue(Lecture::class.java)!!
                        lecture.sub_id = lectureM.sub_id
                        updateLectureFinal(lecture)
                    }
                }

            })
    }

    fun deleteLecture(lecture: Lecture) {
        ref?.child("subjects")
            .child(lecture.sub_id)
            .child("lect_ids")
            .child(lecture.id)
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) checkSubject(lecture.sub_id)
            }
        ref?.child("lectures")
            .child(lecture.id)
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    AppApplication.context?.toast("Lecture Deleted Successfully")

                } else AppApplication.context?.toast("Lecture Deletion Failed")
            }

    }

    fun checkSubject(key: String?) {
        ref?.child("subjects")
            .child(key!!)
            .child("lect_ids")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (!p0.exists()) {
                        if (!p0.hasChildren()) {
                            removeSubject(key!!)
                        }
                    }
                }

            })
    }

    fun removeSubject(key: String) {
        ref?.child("subjects")
            .child(key)
            .removeValue()
    }

    fun addAttendance(id: String, sub_id: String, attended: Boolean) {
        if (attended) {
            addCurrentAttendance(id, sub_id)
        }
        addTotalAttendance(id, sub_id)
    }

    private fun addCurrentAttendance(id: String, sub_id: String) {

        Log.i("ID", id)
        ref?.child("lectures")
            .child(id).child("c_attendance")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.i("DatabaseStatus","ADD-Current-Failed "+ p0?.message.toString())
                    notifBuilder.buildErrorNotif("ADD-Current-Failed "+ p0.message.toString(),-1)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        Log.i("P0VALUE", p0.value.toString())
                        var att = p0.value.toString().toInt()
                        att = att + 1
                        p0.ref.setValue(att).addOnCompleteListener {
                            var str: String = ""
                            if (it.isSuccessful) {
                                str = "Current attendance incremented Successfully"
                            } else {
                                str = "Current attendance updation failed"
                            }
                            AppApplication?.context?.toast(str)
                        }
                    } else Log.i("DATAVALUE", "NULL")

                }

            })
        ref?.child("subjects")
            .child(sub_id)
            .child("c_attendance")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.i("DatabaseStatus","ADD-Current-Failed "+ p0?.message.toString())
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        Log.i("P0VALUE", p0.value.toString())
                        var att = p0.value.toString().toInt()
                        att = att + 1
                        p0.ref.setValue(att).addOnCompleteListener {
                            var str: String = ""
                            if (it.isSuccessful) {
                                str = "Current attendance incremented Successfully"
                            } else {
                                str = "Current attendance updation failed"
                            }
                            AppApplication?.context?.toast(str)
                        }
                    } else Log.i("DATAVALUE", "NULL")

                }


            })

    }

    private fun addTotalAttendance(id: String, sub_id: String) {
        ref?.child("lectures")
            .child(id).child("t_attendance")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.i("DatabaseStatus","ADD-Total-Failed "+ p0.message.toString())
                    notifBuilder.buildErrorNotif("ADD-Total-Failed "+ p0.message.toString(),-1)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    var att = p0.value.toString().toInt()
                    att++
                    p0.ref.setValue(att).addOnCompleteListener {
                        var str = ""
                        if (it.isSuccessful) {
                            str = "Total attendance incremented Successfully"
                        } else {
                            str = "Total attendance updation failed"
                        }
                        AppApplication?.context?.toast(str)
                    }

                }

            })

        ref?.child("subjects")
            .child(sub_id)
            .child("t_attendance")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.i("DatabaseStatus","ADD-Total-Failed "+ p0.message.toString())
                    notifBuilder.buildErrorNotif("ADD-Total-Failed "+ p0.message.toString(),-1)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        Log.i("P0VALUE", p0.value.toString())
                        var att = p0.value.toString().toInt()
                        att = att + 1
                        p0.ref.setValue(att).addOnCompleteListener {
                            var str: String = ""
                            if (it.isSuccessful) {
                                str = "Current attendance incremented Successfully"
                            } else {
                                str = "Current attendance updation failed"
                            }
                            AppApplication?.context?.toast(str)
                        }
                    } else Log.i("DATAVALUE", "NULL")

                }
            })
    }

    fun setLocation(id : String, location : Location)
    {
        ref?.child("lectures")
            .child(id)
            .child("lat")
            .setValue(location.latitude)
            .addOnCompleteListener {
                if (it.isSuccessful) AppApplication?.context?.toast("Latitude Updated")
                else {
                    Log.i("DatabaseStatus","ADD-Location-Failed "+ it.exception?.message.toString())
                    notifBuilder.buildErrorNotif("ADD-Location-Failed"+ it.exception?.message.toString(),-1)
                    AppApplication?.context?.toast("Latitude Updation Failed")
                }
            }
        ref?.child("lectures")
            .child(id)
            .child("lng")
            .setValue(location.longitude)
            .addOnCompleteListener {
                if (it.isSuccessful) AppApplication?.context?.toast("Longitude Updated")
                else
                {
                    Log.i("DatabaseStatus","ADD-Location-Failed "+ it.exception?.message.toString())
                    notifBuilder.buildErrorNotif("ADD-Location-Failed"+ it.exception?.message.toString(),-1)
                    AppApplication?.context?.toast("Longitude Updation Failed")
                }
            }
    }

}