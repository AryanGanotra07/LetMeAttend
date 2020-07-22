package com.attendance.letmeattend.activities.details

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.afollestad.materialdialogs.MaterialDialog
import com.attendance.letmeattend.adapters.LectureNewRecyclerAdapter
import com.attendance.letmeattend.adapters.LectureTimeTableAdapter
import com.attendance.letmeattend.listeners.LectureListeners
import com.attendance.letmeattend.models.LectureModel
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class TimeTableViewModel : ViewModel(), LectureListeners {

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
    val mondayLectureRecyclerAdapter : LectureTimeTableAdapter = LectureTimeTableAdapter()
    val tuesdayLectureRecyclerAdapter : LectureTimeTableAdapter = LectureTimeTableAdapter()
    val wednesdayLectureRecyclerAdapter : LectureTimeTableAdapter = LectureTimeTableAdapter()
    val fridayLectureRecyclerAdapter : LectureTimeTableAdapter = LectureTimeTableAdapter()
    val thursdayLectureRecyclerAdapter : LectureTimeTableAdapter = LectureTimeTableAdapter()
    val saturdayLectureRecyclerAdapter : LectureTimeTableAdapter = LectureTimeTableAdapter()
    val sundayLectureRecyclerAdapter : LectureTimeTableAdapter = LectureTimeTableAdapter()
    private val TAG = "TimeTableViewModel"

   init {

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

    fun addLectureBySubject(lectureModel: LectureModel, sub_id : Int) {
            NewRepository.addLectureBySubject(lectureModel, sub_id).enqueue(object : retrofit2.Callback<LectureModel> {
                override fun onFailure(call: Call<LectureModel>, t: Throwable) {
                    Log.d(TAG, "Lectures adding failed"+t.message)
                }

                override fun onResponse(
                    call: Call<LectureModel>,
                    response: Response<LectureModel>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Lectures adding success")
                        val lectureModel = response.body()
                        when(lectureModel!!.day) {
                            0 -> getLectureByLD(lectureModel, mondayLectures)
                            1 -> getLectureByLD(lectureModel, tuesdayLectures)
                            2 -> getLectureByLD(lectureModel, wednesdayLectures)
                            3 -> getLectureByLD(lectureModel, thursdayLectures)
                            4 -> getLectureByLD(lectureModel, fridayLectures)
                            5 -> getLectureByLD(lectureModel, saturdayLectures)
                            6 -> getLectureByLD(lectureModel, sundayLectures)
                        }
                    }
                    else {
                        Log.d(TAG, "Lectures adding failed"+response.message())
                    }
                }

            })
    }

    private fun getLectureByLD(lectureModel: LectureModel, liveData: MediatorLiveData<List<LectureModel>>) {
        val lectures = liveData.value
        if (lectures!=null) {
            val mLectures = lectures.toMutableList()
            mLectures.add(lectureModel)
            liveData.postValue(mLectures)
        }
        else {
            liveData.postValue(arrayOf(lectureModel).toList())
        }
    }

     fun deleteLecture(lectureModel: LectureModel) {
        NewRepository.deleteLecture(lectureModel).enqueue(object : retrofit2.Callback<JSONObject> {
            override fun onFailure(call: Call<JSONObject>, t: Throwable) {
                Log.d(TAG, " Deleting lecture failed" + t.message)
            }

            override fun onResponse(call: Call<JSONObject>, response: Response<JSONObject>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "lecture Deleted")
                    when (lectureModel.day) {
                        0 -> mondayLectureRecyclerAdapter.deleteLecture(lectureModel)
                        1 -> tuesdayLectureRecyclerAdapter.deleteLecture(lectureModel)
                        2 -> wednesdayLectureRecyclerAdapter.deleteLecture(lectureModel)
                        3 -> thursdayLectureRecyclerAdapter.deleteLecture(lectureModel)
                        4 -> fridayLectureRecyclerAdapter.deleteLecture(lectureModel)
                        5 -> saturdayLectureRecyclerAdapter.deleteLecture(lectureModel)
                        6 -> sundayLectureRecyclerAdapter.deleteLecture(lectureModel)
                    }
                }
                else{
                    Log.d(TAG, "Lecture deletion failed-"+response.message())
                }
            }

        })
    }

    private fun editLectureFromLD(position: Int, lectureModel: LectureModel, ld : MediatorLiveData<List<LectureModel>>) {
        var lectures = ld.value!!.toMutableList()
        lectures[position]= lectureModel!!
        ld.postValue(lectures)
    }



    fun editLecture(position: Int, lectureModel: JsonObject) {
            NewRepository.updateLecture(lectureModel).enqueue(object : retrofit2.Callback<LectureModel> {
                override fun onFailure(call: Call<LectureModel>, t: Throwable) {
                    Log.d(TAG, "Lecture updated failed" + t.message)
                }

                override fun onResponse(
                    call: Call<LectureModel>,
                    response: Response<LectureModel>
                ) {
                   if (response.isSuccessful) {
                       Log.d(TAG, "Lecture update successful")
                       val lectureModel = response.body()
                       when(lectureModel!!.day) {
                           0 -> editLectureFromLD(position,lectureModel,mondayLectures)
                           1 -> editLectureFromLD(position,lectureModel,tuesdayLectures)
                           2 -> editLectureFromLD(position,lectureModel,wednesdayLectures)
                           3 -> editLectureFromLD(position,lectureModel,thursdayLectures)
                           4 -> editLectureFromLD(position,lectureModel,fridayLectures)
                           5 -> editLectureFromLD(position,lectureModel,saturdayLectures)
                           6 -> editLectureFromLD(position,lectureModel,sundayLectures)
                       }
                   }
                    else{
                       Log.d(TAG, "Lecture updated failed" + response.message())
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

    override fun onLectureClick(lecture: LectureModel) {
        TODO("Not yet implemented")
    }


    override fun onLectureEdit(position: Int, lecture: LectureModel) {

    }

    override fun onLectureDelete(lecture: LectureModel) {
//        Log.d(TAG, "Edit lecture")
//        deleteLecture(lecture)
    }


}