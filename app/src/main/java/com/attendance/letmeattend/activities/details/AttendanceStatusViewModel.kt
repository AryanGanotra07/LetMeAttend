package com.attendance.letmeattend.activities.details

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.attendance.letmeattend.adapters.AttendanceStatusRecyclerAdapter
import com.attendance.letmeattend.models.AttendanceStatusModel
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AttendanceStatusViewModel : ViewModel() {

    val attendanceStatusLiveData : MediatorLiveData<ArrayList<AttendanceStatusModel>> = MediatorLiveData()
    val attendanceStatusAdapter : AttendanceStatusRecyclerAdapter = AttendanceStatusRecyclerAdapter()
    private val TAG = "AttendanceStatusViewModel"
    private val NewRepository = NewRepositoryClass()

    fun getAllAttendanceStatus(lect_id : Int) {
        NewRepository.getAttendanceByLecture(lect_id).enqueue(object  : Callback<ArrayList<AttendanceStatusModel>> {
            override fun onFailure(call: Call<ArrayList<AttendanceStatusModel>>, t: Throwable) {
                Log.d(TAG, "Attendance list failed" + t.message)
            }

            override fun onResponse(
                call: Call<ArrayList<AttendanceStatusModel>>,
                response: Response<ArrayList<AttendanceStatusModel>>
            ) {
               if (response.isSuccessful) {
                   Log.d(TAG, "Attendance Getting successful")
                   val statuses = response.body()
                   attendanceStatusLiveData.postValue(statuses)
               }
                else {
                   Log.d(TAG, "Attendane getting failed"+response.message())
               }
            }


        })
    }

    fun updateAttendanceStatusModel(lect_id : Int, json : JsonObject, position : Int) {
        NewRepository.putAttendance(lect_id, json).enqueue(object : Callback<AttendanceStatusModel> {
            override fun onFailure(call: Call<AttendanceStatusModel>, t: Throwable) {
                Log.d(TAG, "Attendance updation failed"+t.message)
            }

            override fun onResponse(
                call: Call<AttendanceStatusModel>,
                response: Response<AttendanceStatusModel>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Attendance Status successful")
                    val status = response.body()
                    attendanceStatusAdapter.setLecture(position, status!!)
                }
                else {
                    Log.d(TAG, "Attendance status updation failed"+response.message())
                }
            }

        })
    }
}