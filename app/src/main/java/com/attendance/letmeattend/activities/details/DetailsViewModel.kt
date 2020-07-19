package com.attendance.letmeattend.activities.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.attendance.letmeattend.adapters.LectureNewRecyclerAdapter
import com.attendance.letmeattend.adapters.SubjectRecyclerAdapter
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.authentication.FirebaseLogin
import com.attendance.letmeattend.models.AttendanceCriteria
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.network.EndPoints
import com.attendance.letmeattend.network.RetrofitServiceBuilder
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DetailsViewModel : ViewModel(), SubjectListeners {
    private val service  = RetrofitServiceBuilder.buildServiceWithAuth(EndPoints::class.java)
    private val TAG = "DetailsViewModel"
    var attendanceCriteria : Int = 0
    val progressVisibility : MediatorLiveData<Int> = MediatorLiveData<Int>()
    val attendanceCriteriaLiveData : MediatorLiveData<AttendanceCriteria> = MediatorLiveData<AttendanceCriteria>()
    val subjectRecyclerAdapter : SubjectRecyclerAdapter = SubjectRecyclerAdapter()
    val lectureRecyclerAdapter : LectureNewRecyclerAdapter = LectureNewRecyclerAdapter()
    val subjectsLiveData : MediatorLiveData<List<SubjectModel>> = MediatorLiveData()
    val lecturesLiveData : MediatorLiveData<List<LectureModel>> = MediatorLiveData()
    val fragmentDisplayer : MediatorLiveData<Fragment> = MediatorLiveData()
    init {
        progressVisibility.value = View.GONE
        attendanceCriteriaLiveData.value = AttendanceCriteria(75)
        subjectRecyclerAdapter.setClickListener(this)
        getUserAttendanceCriteria()
        getUserCourses()
        getUserLecturesToday()
    }
    val updateAttendanceCriteriaClickListener = View.OnClickListener {

    }

    fun deleteSubject(subject: SubjectModel) {
        NewRepository.deleteSubject(subject).enqueue(object : Callback<JSONObject> {
            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                Log.d(TAG, " Deleting subject failed" + t.message)
            }

            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Subject Deleted")
                   val subjects =  subjectsLiveData.value?.toMutableList()
                    subjects?.remove(subject)
                    subjectsLiveData.postValue(subjects)
                }
                else{
                    Log.d(TAG, "SUbject deletion failed-"+response.message())
                }
            }

        })
    }

    fun updateSubject(position: Int,subject : HashMap<String, Any>) {
        NewRepository.updateSubject(subject).enqueue(object : Callback<SubjectModel> {
            override fun onFailure(call: Call<SubjectModel>, t: Throwable) {
                Log.d(TAG, "Subject updation failed" + t.message)
            }

            override fun onResponse(call: Call<SubjectModel>, response: Response<SubjectModel>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Subject updated successuly")
                    val subjectModel = response.body()
                    var subjects = subjectsLiveData.value!!.toMutableList()
                    subjects.set(position, subjectModel!!)
                    subjectsLiveData.postValue(subjects)
//                    if (subjects.isNullOrEmpty()) {
//                        subjects = listOf(subjectModel!!)
//                        subjectsLiveData.postValue(subjects)
//                    }
//                    else {
//                        val subjectsML = subjects.toMutableList()
//                        subjectsML.add(0, subjectModel!!)
//                        subjectsLiveData.postValue(subjectsML)
//
//                    }
//                    var subjectsArrayList : MutableList<SubjectModel> = ArrayList()
//                    if (subjects != null)
//                    {
//                        subjectsArrayList = subjects!!.toMutableList()
//                    }
//                    if (subjectsArrayList!=null) {
//                        subjectsArrayList.add(0, subjectModel!!)
//                        subjectsLiveData.postValue(subjectsArrayList)
//                    }
//                    subjectRecyclerAdapter.addSubject(subjectModel!!)

//                    subjectRecyclerAdapter.addSubject(subjectModel!!)
                }
                else {
                    Log.d(TAG, "Subject adding failed" + response.message())
                }
            }

        })
    }

    fun addSubject(subject : HashMap<String, Any>) {
        NewRepository.addSubject(subject).enqueue(object : Callback<SubjectModel> {
            override fun onFailure(call: Call<SubjectModel>, t: Throwable) {
                Log.d(TAG, "Subject adding failed" + t.message)
            }

            override fun onResponse(call: Call<SubjectModel>, response: Response<SubjectModel>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Subject added successuly")
                    val subjectModel = response.body()
                    var subjects = subjectsLiveData.value
                    if (subjects.isNullOrEmpty()) {
                        subjects = listOf(subjectModel!!)
                        subjectsLiveData.postValue(subjects)
                    }
                    else {
                        val subjectsML = subjects.toMutableList()
                        subjectsML.add(0, subjectModel!!)
                        subjectsLiveData.postValue(subjectsML)

                    }
//                    var subjectsArrayList : MutableList<SubjectModel> = ArrayList()
//                    if (subjects != null)
//                    {
//                        subjectsArrayList = subjects!!.toMutableList()
//                    }
//                    if (subjectsArrayList!=null) {
//                        subjectsArrayList.add(0, subjectModel!!)
//                        subjectsLiveData.postValue(subjectsArrayList)
//                    }
//                    subjectRecyclerAdapter.addSubject(subjectModel!!)

//                    subjectRecyclerAdapter.addSubject(subjectModel!!)
                }
               else {
                    Log.d(TAG, "Subject adding failed" + response.message())
                }
            }

        })
    }
    private fun updateAttendanceCriteria(attendance : Int) : Call<JSONObject>  {
        return service.updateUserAttendanceCriteria(AttendanceCriteria(attendance))
    }

    fun getDay() : String {
        val sdf = SimpleDateFormat("EEEE")
        val d = Date()
        val dayOfTheWeek: String = sdf.format(d)
        return dayOfTheWeek
    }

    fun logout() {
        NewRepository.logout()
    }

    fun updateAttendanceCriteriaMethod() {
        progressVisibility.value = View.VISIBLE
        Log.d(TAG, "Clicked update save listener with-" + attendanceCriteria)
        android.os.Handler().postDelayed(Runnable {
            progressVisibility.value = View.GONE
            Log.d(TAG, " Attendance Updated")
        }, 2000)
        updateAttendanceCriteria(attendanceCriteria).enqueue(object : Callback<JSONObject> {
            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                progressVisibility.value = View.GONE
                Log.d(TAG, " Attendance Not Updated")
            }

            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                progressVisibility.value = View.GONE
                if (response.isSuccessful) {
                    Log.d(TAG, " Attendance Updated")
                }
            }

        })
    }

    fun getUserAttendanceCriteria() {
        service.getUserAttendanceCriteria().enqueue(object : Callback<AttendanceCriteria> {
            override fun onFailure(call: Call<AttendanceCriteria>, t: Throwable) {
                Log.d(TAG, " Attendance getting failed")
            }

            override fun onResponse(
                call: Call<AttendanceCriteria>,
                response: Response<AttendanceCriteria>
            ) {
                val attendanceCriteria = response.body()
                attendanceCriteriaLiveData.value = attendanceCriteria
                Log.d(TAG, " Attendance Updated done - " + attendanceCriteria?.attendanceCriteria)
            }

        })
    }

    fun getUserCourses() {
        Log.d(TAG, " Getting subjects list")
        service.getSubjects().enqueue(object  : Callback<List<SubjectModel>> {
            override fun onFailure(call: Call<List<SubjectModel>>, t: Throwable) {
                Log.d(TAG, " Subject getting failed-"+t.message)
            }

            override fun onResponse(
                call: Call<List<SubjectModel>>,
                response: Response<List<SubjectModel>>
            ) {
                if (response.isSuccessful) {
                    val subjects = response.body()
                    subjectsLiveData.postValue(subjects)
//                    Log.d(TAG, " Got subjects with length - " + subjects!!.get(0).lectures.size)
                }
                else {
                    Log.d(TAG, "Subject Response failed..")
                }
            }

        })
    }

    fun refreshData() {
        getUserAttendanceCriteria()
        getUserCourses()
        getUserLecturesToday()
    }

    fun showTimetable() {
        Log.d(TAG, " Launching timetable")
       fragmentDisplayer.postValue(TimeTable())
    }
    fun getUserLecturesToday() {
        Log.d(TAG, " Getting subjects list")
        service.getLecturesToday("today").enqueue(object  : Callback<List<LectureModel>> {
            override fun onFailure(call: Call<List<LectureModel>>, t: Throwable) {
                Log.d(TAG, " Subject getting failed-"+t.message)

            }

            override fun onResponse(
                call: Call<List<LectureModel>>,
                response: Response<List<LectureModel>>
            ) {
                if (response.isSuccessful) {
                    val lectures = response.body()
//                    lectures!!.forEach { lecture ->
//                        lecture.color = "#000000"
//                        lecture.name = "Maths"
//                      }
                    lecturesLiveData.postValue(lectures)
                    Log.d(TAG, " Got lectures with length - " + lectures!!.size)
                }
                else {
                    Log.d(TAG, "lectures Response failed..")
                }
            }

        })
    }

    override fun onSubjectEdit(subjectModel: SubjectModel) {

    }

    override fun onSubjectDelete(subjectModel: SubjectModel) {
        Log.d(TAG, "Deleting subject")
        deleteSubject(subjectModel)
    }

    override fun onSubjectClicked(subjectModel: SubjectModel) {
        Log.d(TAG, "Clicked subject")
        val fragment = TimeTable()
        val bundle = Bundle()
        bundle.putParcelable("subject", subjectModel)
        fragment.arguments = bundle
        fragmentDisplayer.postValue(fragment)
    }
}