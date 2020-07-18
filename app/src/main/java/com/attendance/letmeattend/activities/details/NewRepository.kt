package com.attendance.letmeattend.activities.details

import android.content.Intent
import android.util.Log
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.authentication.FirebaseLogin
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.network.EndPoints
import com.attendance.letmeattend.network.RetrofitServiceBuilder
import com.attendance.letmeattend.sharedpreferences.LocalRepository
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject
import retrofit2.Call
import java.util.HashMap

object NewRepository {

    val TAG = "NewRepository"

    init {
        Log.d(TAG, "Initialized")
    }

    var service  = RetrofitServiceBuilder.buildServiceWithAuth(EndPoints::class.java)


    fun getAllLectures() : Call<List<LectureModel>> {
        return service.getAllLectures()
    }

    fun addSubject(subject : HashMap<String, Any>) : Call<SubjectModel> {
        return service.addSubject(subject)
    }

    fun deleteSubject(subjectModel: SubjectModel) : Call<JSONObject> {
        return service.deleteSubject(subjectModel.id)
    }

    fun updateSubject(subject : HashMap<String, Any>) : Call<SubjectModel> {
        return service.updateSubject(subject)
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        LocalRepository.logout()
        val intent = Intent(AppApplication.context, FirebaseLogin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        AppApplication.context!!.startActivity(intent)
    }

    fun refreshService(){
        Log.d(TAG, "Refreshed")

        service  = RetrofitServiceBuilder.buildServiceWithAuth(EndPoints::class.java)
    }
}