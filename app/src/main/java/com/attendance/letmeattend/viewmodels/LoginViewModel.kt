package com.attendance.letmeattend.viewmodels

import androidx.lifecycle.ViewModel
import com.attendance.letmeattend.models.LoginResponse
import com.attendance.letmeattend.models.User
import com.attendance.letmeattend.network.EndPoints
import com.attendance.letmeattend.network.RetrofitServiceBuilder
import org.json.JSONObject
import retrofit2.Call

class LoginViewModel() : ViewModel() {

    private val service  = RetrofitServiceBuilder.buildService(EndPoints::class.java)

    fun registerUser(user: User) : Call<LoginResponse> {
        return service.registerUser(user)
    }

}