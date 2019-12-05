package com.attendance.letmeattend.FirebaseRepository

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError
import java.util.logging.Handler





class FirebaseDatabaseLiveData(var query: DatabaseReference?) : LiveData<DataSnapshot>() {


    private val eventListener : MyValueEventListener = MyValueEventListener();
    private var listenerRemovePending : Boolean = false
    private val handler = android.os.Handler()
    private val removeListener = Runnable {
        query?.removeEventListener(eventListener)
        listenerRemovePending = false
    }




    companion object {
        val LOG_TAB = "FirebaseDatabaseLiveData"
    }

    override fun onActive() {
        super.onActive()
        if(listenerRemovePending) {
           handler.removeCallbacks(removeListener)
        }
        else{
            query?.addValueEventListener(eventListener)
        }
        listenerRemovePending = false
    }

    override fun onInactive() {
        super.onInactive()
        handler.postDelayed(removeListener, 2000)
        listenerRemovePending = true
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