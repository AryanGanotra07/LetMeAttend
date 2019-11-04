package com.attendance.letmeattend.FirebaseRepository

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError





class FirebaseDatabaseLiveData(var query: DatabaseReference?) : LiveData<DataSnapshot>() {


    private val eventListener : MyValueEventListener = MyValueEventListener();




    companion object {
        val LOG_TAB = "FirebaseDatabaseLiveData"
    }

    override fun onActive() {
        super.onActive()
        query?.addValueEventListener(eventListener)
    }

    override fun onInactive() {
        super.onInactive()
        query?.removeEventListener(eventListener)
    }

    private inner class MyValueEventListener : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            value = dataSnapshot
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(LOG_TAB, "Can't listen to query $query", databaseError.toException())
        }
    }
}