package com.attendance.letmeattend.LoginScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.attendance.letmeattend.R
import com.firebase.ui.auth.AuthUI
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
                skin_signin_textview.id->
                    Log.i("UserScreen","Entered")



            }



    }


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_login)
       // setTheme(R.style.FirebaseAuthUi)
       // createSignInIntent()

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
                Log.i("UserScreen","Entered")
            }
            else{
                Log.i("UserScreen","Failed")
            }
        }
        if(requestCode== RC_SIGN_IN_EMAIL){
            if(requestCode==Activity.RESULT_OK){
                Log.i("UserScreen","Entered")
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