package com.attendance.letmeattend.activities.details

import android.content.Intent
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.authentication.FirebaseLogin
import com.attendance.letmeattend.models.AttendanceStatusModel
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.models.SubjectQuery
import com.attendance.letmeattend.network.EndPoints
import com.attendance.letmeattend.network.RetrofitServiceBuilder
import com.attendance.letmeattend.sharedpreferences.LocalRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.util.HashMap

class NewRepositoryClass {
    lateinit var service : EndPoints
    constructor() {
        Log.d(TAG, "Initialized")
        service  = RetrofitServiceBuilder.buildServiceWithAuth(EndPoints::class.java, LocalRepository.getAuthenticationToken())
    }

    val TAG = "NewRepository"

    init {

        //service  = RetrofitServiceBuilder.buildServiceWithAuth(EndPoints::class.java)
    }




    fun getAllLectures() : Call<List<LectureModel>> {
        return service!!.getAllLectures()
    }

    fun addAttendanceStatus(lect_id : Int, json : JsonObject) : Call<AttendanceStatusModel> {
        return service!!.postAttendanceByLecture(lect_id, json)
    }

    fun getAttendanceByLecture(lect_id: Int) : Call<ArrayList<AttendanceStatusModel>> {
        return service!!.getAttendanceByLecture(lect_id)
    }

    fun logoutApi() : Call<JsonObject> {
        val json = JsonObject()
        json.addProperty("logout", true)
        return service!!.logout(json)
    }

    fun putAttendance(lect_id : Int, json : JsonObject) : Call<AttendanceStatusModel> {
        return service!!.putAttendance(lect_id, json)
    }

    fun addLectureBySubject(lectureModel: LectureModel, sub_id : Int) : Call<LectureModel> {
return service!!.addLectureBySubject(sub_id, lectureModel.toJSON())
    }

    fun addSubject(subject : HashMap<String, Any>) : Call<SubjectModel> {
        return service!!.addSubject(subject)
    }

    fun deleteSubject(subjectModel: SubjectModel) : Call<JSONObject> {
        return service!!.deleteSubject(subjectModel.id)
    }

    fun updateSubject(subject : HashMap<String, Any>) : Call<SubjectModel> {
        return service!!.updateSubject(subject)
    }

    fun getSubjectsByName(name : String) : Call<List<SubjectQuery>> {
        return service!!.getSubjectsByName(name)
    }

    fun getLecturesBySubject(id : Int) : Call<List<LectureModel>> {
        return service!!.getLecturesBySubject(id)
    }

    fun addSubjectWithLectures(json : JsonObject) : Call<SubjectModel> {
        return service!!.addSubjectWithLectures(json)
    }

    fun deleteLecture(lectureModel: LectureModel) : Call<JSONObject> {
        return service!!.deleteLecture(lectureModel.id)
    }

    fun updateLecture(lectureModel: JsonObject) : Call<LectureModel> {
        return service!!.updateLecture(lectureModel)
    }

    fun logout(){

        FirebaseAuth.getInstance().signOut()
        LocalRepository.logout()
        val intent = Intent(AppApplication.context, FirebaseLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        AppApplication.context!!.startActivity(intent)
    }

    fun refreshService(endPoints: EndPoints){
        Log.d(TAG, "Refreshed")

        service  = endPoints
    }
}