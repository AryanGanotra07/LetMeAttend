package com.attendance.letmeattend.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.attendance.letmeattend.R
import com.attendance.letmeattend.application.AppApplication


object MyNotificationChannel {

    val NO_RESPONSE_CHANNEL_ID = "noResponseID"
    val CHANNEL_ID = "AR123"
    val ENTRANCE_CHANNEL_ID = "entranceNotificationID"
    val ATTENDANCE_STATUS_CHANNEL_ID = "attendanceStatusID"
    val FIREBASE_FOREGROUND_CHANNEL_ID = "firebaseChannelID"
    val MARK_ATTENDANCE_CHANNEL_ID = "markAttendanceChannelID"

    private fun createNotifChannel()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppApplication.context?.getString(R.string.channel_name)
            val descriptionText = AppApplication?.context?.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
               setLightColor(Color.GREEN)
                setVibrationPattern(
                    longArrayOf(
                        100,
                        200,
                        300,
                        400,
                        500,
                        400,
                        300,
                        200,
                        400
                    )
                )
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
               AppApplication?.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createEntryExitNotificationChannel () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppApplication.context?.getString(R.string.channel_name)
            val descriptionText = AppApplication?.context?.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(ENTRANCE_CHANNEL_ID, ENTRANCE_CHANNEL_ID, importance).apply {
                description = descriptionText
                enableVibration(true)
                setLightColor(Color.GREEN)
                setVibrationPattern(
                    longArrayOf(
                        100,
                        200,
                        300,
                        400,
                        500,
                        400,
                        300,
                        200,
                        400
                    )
                )
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                AppApplication?.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createAttendanceMarkedStatusNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppApplication.context?.getString(R.string.channel_name)
            val descriptionText = AppApplication?.context?.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(ATTENDANCE_STATUS_CHANNEL_ID, ATTENDANCE_STATUS_CHANNEL_ID, importance).apply {
                description = descriptionText
                enableVibration(true)
                setLightColor(Color.GREEN)
                setVibrationPattern(
                    longArrayOf(
                        100,
                        200,
                        300,
                        400,
                        500,
                        400,
                        300,
                        200,
                        400
                    )
                )
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                AppApplication?.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun createNoResponseNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppApplication.context?.getString(R.string.channel_name)
            val descriptionText = AppApplication?.context?.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NO_RESPONSE_CHANNEL_ID, NO_RESPONSE_CHANNEL_ID, importance).apply {
                description = descriptionText
                enableVibration(true)
                setLightColor(Color.GREEN)
                setVibrationPattern(
                    longArrayOf(
                        100,
                        200,
                        300,
                        400,
                        500,
                        400,
                        300,
                        200,
                        400
                    )
                )
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                AppApplication?.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createFirebaseNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppApplication.context?.getString(R.string.channel_name)
            val descriptionText = AppApplication?.context?.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_NONE
            val channel = NotificationChannel(FIREBASE_FOREGROUND_CHANNEL_ID, FIREBASE_FOREGROUND_CHANNEL_ID, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                AppApplication?.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createMarkAttendanceNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = AppApplication.context?.getString(R.string.channel_name)
            val descriptionText = AppApplication?.context?.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_NONE
            val channel = NotificationChannel(MARK_ATTENDANCE_CHANNEL_ID, MARK_ATTENDANCE_CHANNEL_ID, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                AppApplication?.context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createAllNotificationChannels() {
        createNotifChannel()
        createEntryExitNotificationChannel()
        createAttendanceMarkedStatusNotificationChannel()
        createNoResponseNotificationChannel()
        createFirebaseNotificationChannel()
        createMarkAttendanceNotificationChannel()
    }
}