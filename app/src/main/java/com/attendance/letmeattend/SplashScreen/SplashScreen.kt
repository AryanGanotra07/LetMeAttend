package com.attendance.letmeattend.SplashScreen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MediatorLiveData
import com.attendance.letmeattend.authentication.FirebaseLogin
import com.attendance.letmeattend.activities.EnterDetailsActivity
import com.attendance.letmeattend.firebase.Repository
import com.attendance.letmeattend.activities.MapFragment
import com.attendance.letmeattend.models.CollegeLocation
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.google.firebase.auth.FirebaseAuth


private lateinit var collegeLocationLiveData : MediatorLiveData<CollegeLocation>

class SplashScreen : AppCompatActivity() {

    private val LOCATION_PERMISSION = 12
    private final val TAG = "SplashScreen"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyNotificationChannel.createAllNotificationChannels()
        Log.d(TAG, FirebaseAuth.getInstance().currentUser.toString())
        if (FirebaseAuth.getInstance().currentUser != null) {
            collegeLocationLiveData = Repository.getCollegeLocation()
            collegeLocationLiveData.observe(this, collegeLocationObserver)
//            if(getLocationPermission()) {
//                startActivity(Intent(this, EnterDetailsActivity::class.java))
//                finish()
//            }

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

    private val collegeLocationObserver = androidx.lifecycle.Observer<CollegeLocation> {
        if (it != null) {
            val collegeLocation = it as CollegeLocation
            if (collegeLocation != null) {
                if (collegeLocation.lat == null
                    || collegeLocation.lng == null
                    || collegeLocation.center == null
                ) {

                    openMap()
                } else {
                    initiateUser()
                }
            }
        }
        else {
            openMap()
        }
    }

    private fun openMap() {

        collegeLocationLiveData.removeObserver(collegeLocationObserver)
        startActivity(Intent(this, MapFragment::class.java))
        finish()
    }

    private fun initiateUser() {
        getLocationPermission()
        collegeLocationLiveData.removeObserver(collegeLocationObserver)
        startActivity(Intent(this, EnterDetailsActivity::class.java))
        finish()
    }


}