package com.attendance.letmeattend.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.attendance.letmeattend.utils.toast

class AlarmReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
      context?.toast("Hello Recieved Message")
    }

}
