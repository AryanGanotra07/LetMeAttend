package com.attendance.letmeattend.firebase

import android.content.Intent
import android.location.Location
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.helpers.NotificationAlertStatus
import com.attendance.letmeattend.models.*
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.notifications.NotificationBuilder
import com.attendance.letmeattend.alarms.AlarmFunctions
import com.attendance.letmeattend.helpers.AttendanceMarkingStatus
import com.attendance.letmeattend.services.backgroundservices.MyForegroundServiceExecutor
import com.attendance.letmeattend.utils.toast
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FirebaseSetData(val userId: String) {
    //    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val notifBuilder = NotificationBuilder()
    private val ref = userId?.let {
        database.getReference("User").child(it)
    }

    private val TAG = "FirebaseSetData"


  init {
      MyNotificationChannel.createAllNotificationChannels()
  }

    fun addAttendanceStatus(attendanceStatus: AttendanceStatus) {
        val key = ref?.child("attendanceStatus")?.push()?.key
        if (key != null) {
            attendanceStatus.id = key
        }
        val childUpdates = HashMap<String, Any>()
        childUpdates["/attendanceStatus/$key"] = attendanceStatus.toMap()
        ref?.updateChildren(childUpdates)?.addOnCompleteListener {
            if (it.isSuccessful) {


                AppApplication.context?.toast("Attendance Status Added")

            } else AppApplication.context?.toast("Error in adding attendance status")
            AttendanceMarkingStatus.setAttendanceMarking(false)
        }
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
                    deleteAttendanceStatusByLecture(lecture)

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

    fun addAttendance(lecture: Lecture, attended: Boolean) {

        getAttendanceStatusForRecheck(lecture, attended)
        AttendanceMarkingStatus.setLecture(lecture)
        AttendanceMarkingStatus.setAttendanceMarking(true)

    }

    private fun createAttendanceStatus(lecture: Lecture, attended: Boolean ) : AttendanceStatus {
        val attendanceStatus = AttendanceStatus("",lecture.id, lecture.sub_id, attended, lecture.s_time, lecture.e_time, lecture.day)
        return attendanceStatus
    }

    private fun deleteAttendanceStatusById(attendanceStatus: AttendanceStatus) {
        ref?.child("attendanceStatus").child(attendanceStatus.id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                AppApplication?.context?.toast("Attendance status deleted successfully");
            }
            else {
                AppApplication?.context?.toast("Error removing attendance status");
            }
        }
    }
    
    private fun deleteAttendanceStatusByLecture(lecture : Lecture) {
        ref?.child("attendanceStatus").orderByChild("lect_id").equalTo(lecture.id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.hasChildren()) {
                        val attendanceStatusList : ArrayList<AttendanceStatus> = ArrayList()
                        for (attendanceStat in p0.children) {
                            val attendanceStatus = attendanceStat.getValue(AttendanceStatus::class.java)
                            deleteAttendanceStatusById(attendanceStatus!!)
                        }
                        Log.d(TAG,attendanceStatusList.size.toString())
                    }
                    else {
                        AppApplication?.context?.toast("No attendances found for the deleted lecture");
                    }


                }
            })
    }

    fun getAttendanceStatus(lecture : Lecture, intent : Intent) {
        ref?.child("attendanceStatus").orderByChild("lect_id").equalTo(lecture.id)
//            orderByChild("s_time").equalTo(lecture.s_time)
//            .orderByChild("e_time").equalTo(lecture.e_time)
//            .orderByChild("last_marked").orderByValue()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.hasChildren()) {
                        val attendanceStatusList : ArrayList<AttendanceStatus> = ArrayList()
                        for (attendanceStat in p0.children) {
                            val attendanceStatus = attendanceStat.getValue(AttendanceStatus::class.java)
                            if (attendanceStatus!!.s_time == lecture.s_time
                                && attendanceStatus!!.day == lecture.day
                                && attendanceStatus!!.last_marked.date == Calendar.getInstance().time.date) {

                                attendanceStatusList.add(attendanceStatus!!)
                            }
                            Log.d(TAG, "Attendence status list size is "+attendanceStatusList.size)


                            notifBuilder.removeNotification(lecture.id.hashCode())
                            AppApplication?.context?.stopService(intent)

                            if (attendanceStatusList.isEmpty()
                                && (AttendanceMarkingStatus.getLecture().id != lecture.id
                                        || !AttendanceMarkingStatus.getAttendanceMarking())) {
                                AlarmFunctions.execute(intent,true)
                                //executeAlarmFunction(intent)
                            }
                            else {
                                AlarmFunctions.execute(intent, false)
                            }
                        }
                        Log.d(TAG,attendanceStatusList.size.toString())
                    }
                    else {

                        notifBuilder.removeNotification(lecture.id.hashCode())
                        AppApplication?.context?.stopService(intent)
                        Log.d(TAG, "No attendance found for this lecture")
                        if(AttendanceMarkingStatus.getLecture().id != lecture.id
                            || !AttendanceMarkingStatus.getAttendanceMarking())
                           // executeAlarmFunction(intent)
                                AlarmFunctions.execute(intent,true)
                    }

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    private fun executeAlarmFunction(intent: Intent) {
        val newIntent = Intent(AppApplication?.context!!, MyForegroundServiceExecutor :: class.java)
        newIntent.putExtra("intent", intent)
        val functionExecutor = MyForegroundServiceExecutor()
        functionExecutor.enqueuework(AppApplication?.context!!, newIntent)

    }

    fun getAttendanceStatusForRecheck(lecture : Lecture, attended: Boolean) {
        ref?.child("attendanceStatus").orderByChild("lect_id").equalTo(lecture.id)
//            orderByChild("s_time").equalTo(lecture.s_time)
//            .orderByChild("e_time").equalTo(lecture.e_time)
//            .orderByChild("last_marked").orderByValue()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.hasChildren()) {
                        val attendanceStatusList : ArrayList<AttendanceStatus> = ArrayList()
                        for (attendanceStat in p0.children) {
                            val attendanceStatus = attendanceStat.getValue(AttendanceStatus::class.java)
                            if (attendanceStatus!!.s_time == lecture.s_time
                                && attendanceStatus!!.day == lecture.day
                                && attendanceStatus!!.last_marked.date == Calendar.getInstance().time.date) {

                                attendanceStatusList.add(attendanceStatus!!)
                            }
                            Log.d(TAG, "Attendence status list size is "+attendanceStatusList.size)
                            if (attendanceStatusList.isEmpty()) {
                                if (attended) {
                                    addCurrentAttendance(lecture)
                                }
                                addTotalAttendance(lecture, attended)
                            }
                            else {
                               AppApplication?.context?.toast("Attendance has been marked already!")

                            }
                        }
                        Log.d(TAG,attendanceStatusList.size.toString())
                    }
                    else {
                        if (attended) {
                            addCurrentAttendance(lecture)
                        }
                        addTotalAttendance(lecture, attended)
                    }

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    private fun addCurrentAttendance(lecture : Lecture) {

        val id = lecture.id
        val sub_id = lecture.sub_id
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
                                notifBuilder.buildAttendanceStatusNotification(
                                    lecture,
                                    true,
                                    notifBuilder.ATTENDANCE_STATUS_NOTIF_ID )
                                addAttendanceStatus(createAttendanceStatus(lecture, true))

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

    private fun addTotalAttendance(lecture: Lecture, attended: Boolean) {
        val id = lecture.id
        val sub_id = lecture.sub_id
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

                            if (!attended) {
                                notifBuilder.buildAttendanceStatusNotification(
                                    lecture,
                                    false,
                                    notifBuilder.ATTENDANCE_STATUS_NOTIF_ID
                                )
                                addAttendanceStatus(createAttendanceStatus(lecture, false))

                            }
                            Log.d(TAG, NotificationAlertStatus.isRunning().toString())
                            Log.d(TAG, NotificationAlertStatus.getLecture().name)
                            NotificationAlertStatus.setAttendanceMarkedAlready(lecture!!)
                            Log.d(TAG, NotificationAlertStatus.isRunning().toString())
                            Log.d(TAG, NotificationAlertStatus.getLecture().name)
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

    fun updateAttendance(lecture: Lecture, attended: Boolean) {

        ref?.child("attendanceStatus").orderByChild("lect_id").equalTo(lecture.id)
//            orderByChild("s_time").equalTo(lecture.s_time)
//            .orderByChild("e_time").equalTo(lecture.e_time)
//            .orderByChild("last_marked").orderByValue()
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists() && p0.hasChildren()) {
                        val attendanceStatusList : ArrayList<AttendanceStatus> = ArrayList()
                        for (attendanceStat in p0.children) {
                            val attendanceStatus = attendanceStat.getValue(AttendanceStatus::class.java)
                            if (attendanceStatus!!.s_time == lecture.s_time
                                && attendanceStatus!!.day == lecture.day
                                && attendanceStatus!!.last_marked.date == Calendar.getInstance().time.date) {

                                attendanceStatusList.add(attendanceStatus!!)
                            }
                            Log.d(TAG, "Attendence status list size is "+attendanceStatusList.size)
                            if (attendanceStatusList.isEmpty()) {
                               AppApplication?.context?.toast("No attendance status found")
                            }
                            else if (attendanceStatusList.size == 1 && attended) {
                               val attendanceStatus = attendanceStatusList.get(0)
                                updateAttendanceByAttendanceStatus(attendanceStatus, attended)
                                updateCurrentAttendance(lecture, attended)
                            }
                        }
                        Log.d(TAG,attendanceStatusList.size.toString())
                    }
                    else {
                        AppApplication?.context?.toast("No attendance status found")
                    }

                }

                override fun onCancelled(p0: DatabaseError) {
                    AppApplication?.context?.toast("Failed to find attendane status" + p0.message)
                }
            })
    }

    private fun updateAttendanceByAttendanceStatus(attendanceStatus: AttendanceStatus, attended: Boolean) {
        attendanceStatus.attended = attended
        ref?.
            child("attendanceStatus")
            .child(attendanceStatus.id)
            .setValue(attendanceStatus)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    AppApplication?.context?.toast("Attendance status updated")

                }
                else {
                    AppApplication?.context?.toast("Attendance status updation failed.." + it.exception?.message)
                }
            }
    }

    private fun updateCurrentAttendance(lecture: Lecture, attended: Boolean) {
        val id = lecture.id
        val sub_id = lecture.sub_id
        Log.i("ID", id)
        ref?.child("lectures")
            .child(id).child("c_attendance")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Log.i("DatabaseStatus","Update-Current-Failed "+ p0?.message.toString())
                    notifBuilder.buildErrorNotif("Update-Current-Failed "+ p0.message.toString(),-1)
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        Log.i("P0VALUE", p0.value.toString())
                        var att = p0.value.toString().toInt()
                        if (attended)
                            att = att + 1
                        p0.ref.setValue(att).addOnCompleteListener {
                            var str: String = ""
                            if (it.isSuccessful) {
                                str = "Current attendance decremented Successfully"
                                notifBuilder.buildAttendanceStatusNotification(
                                    lecture,
                                    true,
                                    notifBuilder.ATTENDANCE_STATUS_NOTIF_ID )
                                //addAttendanceStatus(createAttendanceStatus(lecture, true))

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
                        if (attended)
                            att = att + 1
                        p0.ref.setValue(att).addOnCompleteListener {
                            var str: String = ""
                            if (it.isSuccessful) {
                                str = "Current attendance decremented Successfully"
                            } else {
                                str = "Current attendance updation failed"
                            }
                            AppApplication?.context?.toast(str)
                        }
                    } else Log.i("DATAVALUE", "NULL")

                }


            })
    }



}