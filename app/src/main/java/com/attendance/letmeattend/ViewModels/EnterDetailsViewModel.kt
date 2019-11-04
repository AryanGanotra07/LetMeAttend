package com.attendance.letmeattend.ViewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.lifecycle.MediatorLiveData
import com.attendance.letmeattend.FirebaseRepository.Repository
import com.attendance.letmeattend.Model.User
import com.google.firebase.auth.FirebaseAuth


class EnterDetailsViewModel : ViewModel() {


    private val user : MediatorLiveData<User> = Repository.getUserLiveData()



}