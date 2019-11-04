package com.attendance.letmeattend.FirebaseRepository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.attendance.letmeattend.Model.User
import com.google.android.gms.internal.phenotype.zzh.init
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Repository {

   private val DB_REF =
        FirebaseAuth.getInstance().currentUser?.uid?.let {
            FirebaseDatabase.getInstance().getReference(
                it
            )
        }

   private val firebaseDatabaseLiveData = FirebaseDatabaseLiveData(DB_REF);
  private val user : MediatorLiveData<User> = MediatorLiveData()
    init {
        user.addSource(firebaseDatabaseLiveData, Observer {
            if (it != null) {
                Thread(Runnable { user.postValue(it.getValue(User::class.java)) }).start()
            } else {
                user.setValue(null)
            }
        })

    }
    public fun getUserLiveData() : MediatorLiveData<User> {
        return user;
    }
}