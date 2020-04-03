package com.attendance.letmeattend.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.utils.Constants
import com.attendance.letmeattend.utils.Constants.DEVICE_TOKEN
import com.attendance.letmeattend.utils.Constants.PREFERENCE_NAME


object LocalRepository {

    private val sharedPref : SharedPreferences  = AppApplication.context!!.getSharedPreferences(
        PREFERENCE_NAME, Context.MODE_PRIVATE)

    public fun setUser(token : String ) {

        val editor = sharedPref.edit()
        editor.putString(DEVICE_TOKEN, token)
        editor.commit()
    }

    public fun getSavedUser() : String {
        val userId = sharedPref.getString(DEVICE_TOKEN, null)
        return userId
    }
}