package com.attendance.letmeattend.network



import android.util.Log
import com.attendance.letmeattend.activities.details.NewRepository
import com.attendance.letmeattend.authentication.AuthenticationHelper
import com.attendance.letmeattend.sharedpreferences.LocalRepository
import com.attendance.letmeattend.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object RetrofitServiceBuilder {
    private val client = OkHttpClient.Builder()
    private val TAG = "RetrofitServiceBuilder"


    fun <T> buildService(service: Class<T>): T {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.105:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
        return retrofit.create(service)
    }

    fun <T> buildServiceWithAuth(service: Class<T>): T {

        var  token = LocalRepository.getAuthenticationToken()

        Log.d(TAG, "Got token-" +token)
            var interceptor: Interceptor = object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request: Request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + token)
                        .build()
                    return chain.proceed(request)
                }
            }
            client.addInterceptor(interceptor)
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.105:5000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build()
            return retrofit.create(service)
        }
}