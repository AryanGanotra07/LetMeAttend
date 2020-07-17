package com.attendance.letmeattend.activities.details

import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.network.EndPoints
import com.attendance.letmeattend.network.RetrofitServiceBuilder
import retrofit2.Call

object NewRepository {

    val service  = RetrofitServiceBuilder.buildServiceWithAuth(EndPoints::class.java)

    fun getAllLectures() : Call<List<LectureModel>> {
        return service.getAllLectures()
    }
}