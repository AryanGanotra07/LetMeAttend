package com.attendance.letmeattend.SplashScreen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.attendance.letmeattend.authentication.FirebaseLogin
import com.attendance.letmeattend.details.EnterDetailsActivity
import com.attendance.letmeattend.maps.MapFragment
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {

    private val LOCATION_PERMISSION = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyNotificationChannel.createNotifChannel()
        if (FirebaseAuth.getInstance().currentUser != null) {
            if(getLocationPermission()) {
                startActivity(Intent(this, EnterDetailsActivity::class.java))
                finish()
            }

        } else {
            startActivity(Intent(this, FirebaseLogin::class.java))
            finish()
        }
    }
    private fun getLocationPermission(): Boolean {

        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //permission not granted

            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION
            )

            //we will now check for permission granted in onRequestPermissionResult whether the user has granted permission or not
        } else {
            //permission is granted
            return true


        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION) {

           if ( getLocationPermission())
           {
               startActivity(Intent(this@SplashScreen, EnterDetailsActivity::class.java))
               finish()
           }


        }


    }
}