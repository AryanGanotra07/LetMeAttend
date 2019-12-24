package com.attendance.letmeattend.authentication

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.attendance.letmeattend.R
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.details.EnterDetailsActivity
import com.attendance.letmeattend.maps.MapFragment
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.services.alarms.MyAlarmManager
import com.attendance.letmeattend.services.boot.BootCompleteReciever
import com.firebase.ui.auth.AuthUI
import com.judemanutd.autostarter.AutoStartPermissionHelper
import kotlinx.android.synthetic.main.screen_login.*



class FirebaseLogin: AppCompatActivity(),View.OnClickListener {

    override fun onClick(v: View?) {

            when(v?.id){
                email_btn.id->
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())).setTheme(R.style.FirebaseAuthUi).build(),RC_SIGN_IN_EMAIL)
                google_btn.id->
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())).setTheme(R.style.FirebaseAuthUi).build(),
                        RC_SIGN_IN_GOOGLE)
                skin_signin_textview.id->startActivity(Intent(this@FirebaseLogin, MapFragment::class.java))




            }



    }


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_login)
       // setTheme(R.style.FirebaseAuthUi)
       // createSignInIntent()

//        val manufacturer = "xiaomi"
//        if (manufacturer.equals(
//                Build.MANUFACTURER,
//                ignoreCase = true
//            )
//        ) { //this will open auto start screen where user can enable permission for your app
//            val intent1 = Intent()
//            intent1.component = ComponentName(
//                "com.miui.securitycenter",
//                "com.miui.permcenter.autostart.AutoStartManagementActivity"
//            )
//            intent1.setData(Uri.parse("package:" + context!!.getPackageName()));
//            startActivity(intent1)
//        }
        AutoStartPermissionHelper.getInstance().getAutoStartPermission(this)
                val cont = AppApplication?.context
                val receiver = ComponentName(cont, BootCompleteReciever::class.java)


        cont!!.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

       // alarmManager.setAlarm()
        MyNotificationChannel.createNotifChannel()
       // val notificationBuilder = NotificationBuilder()
     //   notificationBuilder.buildNotification(0)
        //val intent = Intent(this, GoogleService::class.java)
        //startService(intent)


    }


    private fun createSignInIntent(){


        //Choosing auth provider
        val providers= arrayListOf(AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        //CreatingSignInIntent
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAlwaysShowSignInMethodScreen(true).setAvailableProviders(providers).setTheme(R.style.FirebaseAuthUi).setLogo(R.drawable.ic_launcher_background).build(),
            RC_SIGN_IN_GOOGLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_SIGN_IN_GOOGLE){
            if(resultCode==Activity.RESULT_OK){
                startActivity(Intent(this@FirebaseLogin, EnterDetailsActivity::class.java))
                finish()
            }
            else{
                Log.i("UserScreen","Failed")
            }
        }
        if(requestCode== RC_SIGN_IN_EMAIL){
            if(requestCode==Activity.RESULT_OK){

                startActivity(Intent(this@FirebaseLogin, EnterDetailsActivity::class.java))
                finish()
            }
            else{
                Log.i("UserScreen","Failed")
            }
        }
    }

    companion object{
        private const val RC_SIGN_IN_GOOGLE=123
        private const val RC_SIGN_IN_EMAIL=123
    }
}