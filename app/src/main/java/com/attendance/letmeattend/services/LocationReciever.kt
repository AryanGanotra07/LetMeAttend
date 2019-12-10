package com.attendance.letmeattend.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.utils.toast

class LocationReciever() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
            if (intent!=null)
            {
                AppApplication.context!!.toast("Lattitude:- "+intent.getDoubleExtra("lat",9.2))
            }
    }
}