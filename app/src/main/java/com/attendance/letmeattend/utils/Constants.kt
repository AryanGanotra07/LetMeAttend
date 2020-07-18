package com.attendance.letmeattend.utils

object Constants {

    public const val PREFERENCE_NAME = "myPref"
    public const val DEVICE_TOKEN = "udt"
    const val ENTRY_KEY = "entry"
    const val JWT_TOKEN = "jwt"
    var auth_token = ""

    fun setAuthToken(token : String) {
        auth_token =token
    }
}