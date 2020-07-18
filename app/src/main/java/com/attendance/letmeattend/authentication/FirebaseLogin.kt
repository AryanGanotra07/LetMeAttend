package com.attendance.letmeattend.authentication

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.attendance.letmeattend.R
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.activities.EnterDetailsActivity
import com.attendance.letmeattend.activities.MapFragment
import com.attendance.letmeattend.activities.details.EnterDetails
import com.attendance.letmeattend.activities.details.NewRepository
import com.attendance.letmeattend.models.CollegeLocation
import com.attendance.letmeattend.models.LoginResponse
import com.attendance.letmeattend.models.User
import com.attendance.letmeattend.notifications.MyNotificationChannel
import com.attendance.letmeattend.services.boot.BootCompleteReciever
import com.attendance.letmeattend.sharedpreferences.LocalRepository
import com.attendance.letmeattend.utils.Constants
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.LoginViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.judemanutd.autostarter.AutoStartPermissionHelper
import kotlinx.android.synthetic.main.screen_login.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FirebaseLogin: AppCompatActivity(),View.OnClickListener {

    private  val TAG = "FirebaseLogin"
    private lateinit var collegeLocationLiveData : MediatorLiveData<CollegeLocation>
    private lateinit var viewModel : LoginViewModel

    override fun onClick(v: View?) {

            when(v?.id){
                email_btn.id->
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())).setTheme(R.style.FirebaseAuthUi).build(),RC_SIGN_IN_EMAIL)
                google_btn.id->
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())).setTheme(R.style.FirebaseAuthUi).build(),
                        RC_SIGN_IN_GOOGLE)
                phone_signin_textview.id->
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
                        arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())).setTheme(R.style.FirebaseAuthUi).build(),
                        RC_SIGN_IN_PHONE)

                skin_signin_textview.id-> {
//                    val collegeLocation = viewModel.getCollegeLocation().value;
//                    if (collegeLocation == null
//                        || collegeLocation.lat == null
//                        ||collegeLocation.lng == null
//                        || collegeLocation.center == null ) {
//
//                        startActivity(Intent(this, com.attendance.letmeattend.activities.MapFragment::class.java))
//                    }

                    val androidId = getUDI()
                    Log.d(TAG, androidId.toString())

                    FirebaseAuth.getInstance().signOut()
                    FirebaseAuth.getInstance().signInWithCustomToken(androidId)
                        .addOnSuccessListener {
                            LocalRepository.setUser(androidId)
                            startActivity(Intent(this@FirebaseLogin, EnterDetailsActivity::class.java))
                        }
                        .addOnFailureListener {
                            AppApplication.context?.toast("Sorry can't login at this moment. Try other methods.")
                            AppApplication.context?.toast(it.message.toString())
                        }


                }




            }



    }

    private fun getUDI() : String {
        val android_id = Settings.Secure.getString(this.getContentResolver(),
            Settings.Secure.ANDROID_ID)
        return android_id
    }


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
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
        MyNotificationChannel.createAllNotificationChannels()
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

                loginUser()
            }
            else{
                Log.i("UserScreen","Gmail Failed")
            }
        }
       else if(requestCode== RC_SIGN_IN_EMAIL){
            if(requestCode==Activity.RESULT_OK){

                loginUser()

            }
            else{
                Log.i("UserScreen","Email Failed")
            }
        }
        else if(requestCode== RC_SIGN_IN_PHONE){
            if(requestCode==Activity.RESULT_OK){

                loginUser()
            }
            else{
                Log.i("UserScreen","Phone Failed")
            }
        }
    }

    private fun loginUser() {

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
                Log.d(TAG, token)
                Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                viewModel.registerUser(User(FirebaseAuth.getInstance().currentUser!!.uid, token!!)).enqueue(object:Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        startActivity(
                            Intent(
                                this@FirebaseLogin,
                                EnterDetails::class.java
                            )
                        )
                        finish()
                    }

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val data = response.body()
//                            val access_token = data?.get("access_token")
                            Log.d(TAG,"got access token-"+data?.access_token)
                            LocalRepository.setAuthenticationToken(data?.access_token!!)
                            Constants.setAuthToken(data?.access_token!!)
                            NewRepository.refreshService()
                            AppApplication.context!!.toast("User registered")
                            startActivity(
                                Intent(
                                    this@FirebaseLogin,
                                    EnterDetails::class.java
                                )
                            )
                            finish()
                        }
                        else {
                            AppApplication.context!!.toast("User not registered -" + response.errorBody())
                            Log.d(TAG, "User not registered -" +response.errorBody())
                        }
                    }

                })
            })



//        collegeLocationLiveData = Repository.getCollegeLocation()
//        collegeLocationLiveData.observe(this, collegeLocationObserver)
    }

    private val collegeLocationObserver  = Observer<CollegeLocation> {
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
        startActivity(Intent(this@FirebaseLogin, MapFragment::class.java))
        finish()
    }

    private fun initiateUser() {
        collegeLocationLiveData.removeObserver(collegeLocationObserver)
        startActivity(Intent(this@FirebaseLogin, EnterDetailsActivity::class.java))
        finish()
    }

    companion object{
        private const val RC_SIGN_IN_GOOGLE=123
        private const val RC_SIGN_IN_EMAIL=123
        private const val RC_SIGN_IN_PHONE=123
    }
}