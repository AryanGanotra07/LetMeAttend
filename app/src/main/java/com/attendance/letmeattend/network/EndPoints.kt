package com.attendance.letmeattend.network

import com.attendance.letmeattend.models.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*

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
    @POST("/subject")
    fun addSubject(@Body subject: HashMap<String, Any>) : Call<SubjectModel>
    @HTTP(method = "DELETE", path = "/subject", hasBody = true)
    fun deleteSubject(@Body id:Int) : Call<JSONObject>
    @PUT("/subject")
    fun updateSubject(@Body subject: HashMap<String, Any>) : Call<SubjectModel>

}