package com.attendance.letmeattend.activities.details

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.attendance.letmeattend.adapters.LectureNewRecyclerAdapter
import com.attendance.letmeattend.models.LectureModel
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class TimeTableViewModel : ViewModel() {

    val mondayLectures : MediatorLiveData<List<LectureModel>> = MediatorLiveData()
    val tuesdayLectures : MediatorLiveData<List<LectureModel>> = MediatorLiveData()
    val wednesdayLectures : MediatorLiveData<List<LectureModel>> = MediatorLiveData()
    val thursdayLectures : MediatorLiveData<List<LectureModel>> = MediatorLiveData()
    val fridayLectures : MediatorLiveData<List<LectureModel>> = MediatorLiveData()
    val saturdayLectures : MediatorLiveData<List<LectureModel>> = MediatorLiveData()
    val sundayLectures : MediatorLiveData<List<LectureModel>> = MediatorLiveData()
    private var monday:List<LectureModel> = ArrayList()
    private var tueday:List<LectureModel> = ArrayList()
    private var wednesday:List<LectureModel> = ArrayList()
    private var thursday:List<LectureModel> = ArrayList()
    private var friday:List<LectureModel> = ArrayList()
    private var saturday:List<LectureModel> = ArrayList()
    private var sunday:List<LectureModel> = ArrayList()
    val mondayLectureRecyclerAdapter : LectureNewRecyclerAdapter = LectureNewRecyclerAdapter()
    val tuesdayLectureRecyclerAdapter : LectureNewRecyclerAdapter = LectureNewRecyclerAdapter()
    val wednesdayLectureRecyclerAdapter : LectureNewRecyclerAdapter = LectureNewRecyclerAdapter()
    val fridayLectureRecyclerAdapter : LectureNewRecyclerAdapter = LectureNewRecyclerAdapter()
    val thursdayLectureRecyclerAdapter : LectureNewRecyclerAdapter = LectureNewRecyclerAdapter()
    val saturdayLectureRecyclerAdapter : LectureNewRecyclerAdapter = LectureNewRecyclerAdapter()
    val sundayLectureRecyclerAdapter : LectureNewRecyclerAdapter = LectureNewRecyclerAdapter()
    private val TAG = "TimeTableViewModel"

    init {
//        getAllLectures()
    }

    fun getAllLectures() {
        NewRepository.getAllLectures().enqueue(object : retrofit2.Callback<List<LectureModel>> {
            override fun onFailure(call: Call<List<LectureModel>>, t: Throwable) {
                Log.d(TAG,"Lectures getting failed"+t.message)
            }

            override fun onResponse(
                call: Call<List<LectureModel>>,
                response: Response<List<LectureModel>>
            ) {
               if (response.isSuccessful) {
                   val lectures = response.body()
//                   lectures!!.forEach { lecture ->
//                       when(lecture.day) {
//                           0 -> monday.plus(lecture)
//                           1 -> tueday.plus(lecture)
//                           2 -> wednesday.plus(lecture)
//                           3 -> thursday.plus(lecture)
//                           4 -> friday.plus(lecture)
//                           5 -> saturday.plus(lecture)
//                           6 -> sunday.plus(lecture)
//                       }
//                   }
                   monday = lectures!!.filter { lectureModel -> lectureModel.day == 0 }
                   tueday = lectures!!.filter { lectureModel -> lectureModel.day == 1 }
                   wednesday = lectures!!.filter { lectureModel -> lectureModel.day == 2 }
                   thursday = lectures!!.filter { lectureModel -> lectureModel.day == 3 }
                   friday = lectures!!.filter { lectureModel -> lectureModel.day == 4 }
                   saturday = lectures!!.filter { lectureModel -> lectureModel.day == 5 }
                   sunday = lectures!!.filter { lectureModel -> lectureModel.day == 6 }

                   mondayLectures.postValue(monday)
                   tuesdayLectures.postValue(tueday)
                   wednesdayLectures.postValue(wednesday)
                   thursdayLectures.postValue(thursday)
                   fridayLectures.postValue(friday)
                   saturdayLectures.postValue(saturday)
                   sundayLectures.postValue(sunday)
                   Log.d(TAG, " Got lectures" + monday.size + " " + tueday.size + " " + wednesday.size + " " + thursday.size + " " + friday.size + " " +saturday.size)

               }
                else {
                   Log.d(TAG, "Lectures getting failed"+response.message())
               }
            }

        })
    }

    fun getLecturesBySubjects(id: Int) {
        NewRepository.getLecturesBySubject(id).enqueue(object : retrofit2.Callback<List<LectureModel>> {
            override fun onFailure(call: Call<List<LectureModel>>, t: Throwable) {
                Log.d(TAG,"Lectures getting failed"+t.message)
            }

            override fun onResponse(
                call: Call<List<LectureModel>>,
                response: Response<List<LectureModel>>
            ) {
                if (response.isSuccessful) {
                    val lectures = response.body()
//                   lectures!!.forEach { lecture ->
//                       when(lecture.day) {
//                           0 -> monday.plus(lecture)
//                           1 -> tueday.plus(lecture)
//                           2 -> wednesday.plus(lecture)
//                           3 -> thursday.plus(lecture)
//                           4 -> friday.plus(lecture)
//                           5 -> saturday.plus(lecture)
//                           6 -> sunday.plus(lecture)
//                       }
//                   }
                    monday = lectures!!.filter { lectureModel -> lectureModel.day == 0 }
                    tueday = lectures!!.filter { lectureModel -> lectureModel.day == 1 }
                    wednesday = lectures!!.filter { lectureModel -> lectureModel.day == 2 }
                    thursday = lectures!!.filter { lectureModel -> lectureModel.day == 3 }
                    friday = lectures!!.filter { lectureModel -> lectureModel.day == 4 }
                    saturday = lectures!!.filter { lectureModel -> lectureModel.day == 5 }
                    sunday = lectures!!.filter { lectureModel -> lectureModel.day == 6 }

                    mondayLectures.postValue(monday)
                    tuesdayLectures.postValue(tueday)
                    wednesdayLectures.postValue(wednesday)
                    thursdayLectures.postValue(thursday)
                    fridayLectures.postValue(friday)
                    saturdayLectures.postValue(saturday)
                    sundayLectures.postValue(sunday)
                    Log.d(TAG, " Got lectures" + monday.size + " " + tueday.size + " " + wednesday.size + " " + thursday.size + " " + friday.size + " " +saturday.size)

                }
                else {
                    Log.d(TAG, "Lectures getting failed"+response.message())
                }
            }

        })
    }


}