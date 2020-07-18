package com.attendance.letmeattend.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.utils.Constants
import com.attendance.letmeattend.utils.Constants.DEVICE_TOKEN
import com.attendance.letmeattend.utils.Constants.ENTRY_KEY
import com.attendance.letmeattend.utils.Constants.JWT_TOKEN
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

    fun setGeofenceState(state : Boolean) {
        val editor= sharedPref?.edit()
        editor!!.putBoolean(ENTRY_KEY,state)
        editor.commit()
    }

    fun getGeofenceState() : Boolean {
        return sharedPref.getBoolean(ENTRY_KEY,true)
    }

    fun setAuthenticationToken(token: String)  {
        val editor = sharedPref.edit()
        editor.putString(JWT_TOKEN, token)
        editor.commit()
    }
    fun getAuthenticationToken() : String {
        return sharedPref.getString(JWT_TOKEN, null)
    }

    fun logout() {
        sharedPref.edit().remove(JWT_TOKEN).commit()
    }
}