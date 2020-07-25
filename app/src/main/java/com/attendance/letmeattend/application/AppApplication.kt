package com.attendance.letmeattend.application

import android.app.Application
import com.google.firebase.FirebaseApp

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        context = this

    }

    companion object {
        var context: AppApplication? = null
        var access_token : String? = null
    }

    fun getContext(): AppApplication? {
        return context
    }

    fun setToken(token : String) {
        access_token = token
    }
}