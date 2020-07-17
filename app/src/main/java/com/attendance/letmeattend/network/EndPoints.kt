package com.attendance.letmeattend.network

import com.attendance.letmeattend.models.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EndPoints {

    @POST("/user/register")
    fun registerUser(@Body user: User) : Call<LoginResponse>
    @POST("/user")
    fun updateUserAttendanceCriteria(@Body attendanceCriteria : AttendanceCriteria) : Call<JSONObject>
    @GET("/user")
    fun getUserAttendanceCriteria() : Call<AttendanceCriteria>
    @GET("/subject/all")
    fun getSubjects() : Call <List<SubjectModel>>
    @GET("/lecture/all")
    fun getLecturesToday(@Query("day") day : String): Call<List<LectureModel>>
    @GET("/lecture/all")
    fun getLecturesByDay(@Query("day") day: Int) : Call<List<LectureModel>>
    @GET("/lecture/all")
    fun getAllLectures() : Call<List<LectureModel>>

}