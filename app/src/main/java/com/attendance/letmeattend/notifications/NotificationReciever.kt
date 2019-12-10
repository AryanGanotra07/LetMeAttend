package com.attendance.letmeattend.notifications

import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.utils.toast

class NotificationReciever() : BroadcastReceiver() {
    val ACTION_YES = "action_yes"
    val ACTION_NO = "action_no"
    val context = AppApplication?.context
    val builder : NotificationBuilder = NotificationBuilder()
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == ACTION_YES)
        {
            context?.toast("YES CLICKED")
            builder.removeNotification(intent.getIntExtra(EXTRA_NOTIFICATION_ID,0))
        }
        else if (intent?.action == ACTION_NO)
        {
            context?.toast("NO CLICKED")
            builder.removeNotification(intent.getIntExtra(EXTRA_NOTIFICATION_ID,0))
        }
    }
}