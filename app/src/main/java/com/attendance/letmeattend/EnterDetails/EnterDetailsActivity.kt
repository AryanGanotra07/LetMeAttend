package com.attendance.letmeattend.EnterDetails

import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.attendance.letmeattend.EnterDetails.TimeTable.SubjectDialogHelper
import com.attendance.letmeattend.FirebaseRepository.Repository
import com.attendance.letmeattend.Model.User
import com.attendance.letmeattend.R
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.database.FirebaseDatabase
import com.ramotion.fluidslider.FluidSlider
import kotlinx.android.synthetic.main.enter_details_activity.*
import kotlinx.android.synthetic.main.enter_details_activity.view.*
import java.sql.Time
import java.util.zip.Inflater


class EnterDetailsActivity: AppCompatActivity(), SaveClickListener {

    val fragmentManager:FragmentManager=supportFragmentManager
    val attendanceCriteriaFragment:AttendanceCriteriaFragment= AttendanceCriteriaFragment()

    override fun onSave(attendance: Int) {
      //  add_btn.visibility = View.VISIBLE

       val fragment : Fragment? = fragmentManager.findFragmentByTag("attendance");
        fragment?.let { fragmentManager.beginTransaction().remove(it).commit() }

        val tabLayoutAdapter:TabLayoutAdapter= TabLayoutAdapter(fragmentManager,6)
        val view_pager:ViewPager=findViewById(R.id.view_pager) as ViewPager
        view_pager.adapter=tabLayoutAdapter
        tab_layout.setupWithViewPager(view_pager)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enter_details_activity)



//        val lecture : User.Day.Lecture = User.Day.Lecture("Lecture",User.Day.Lecture.CTime("start","end"), 75, User.Day.Lecture.Attendance
//        (23,45))
//
//
//        val lectures : ArrayList<User.Day.Lecture> = ArrayList()
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//        lectures.add(lecture)
//
//
//        val mon : User.Day = User.Day(lectures)
//        val tues : User.Day = User.Day(lectures)
//        val wed : User.Day = User.Day(lectures)
//        val thurs : User.Day = User.Day(lectures)
//        val fri : User.Day = User.Day(lectures)
//        val sat : User.Day = User.Day(lectures)
//
//        val days = ArrayList<User.Day>()
//        days.add(mon)
//        days.add(tues)
//        days.add(wed)
//        days.add(thurs)
//        days.add(fri)
//        days.add(sat)
//
//
//
//
//
//
//
//        var user : com.attendance.letmeattend.Model.User = com.attendance.letmeattend.Model.User("1001",
//            75,
//            User.CollegeLocation(123,123.4),
//            days
//            )
//
//
//        FirebaseDatabase.getInstance().reference.child("User").child("11001").setValue(user)
//






        val fragmentTransaction:FragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_layout,attendanceCriteriaFragment,"attendance")
        fragmentTransaction.addToBackStack("attendance")
        fragmentTransaction.commit()



//
//        val tabLayoutAdapter:TabLayoutAdapter= TabLayoutAdapter(supportFragmentManager,7)
//        val view_pager:ViewPager=findViewById(R.id.view_pager) as ViewPager
//        view_pager.adapter=tabLayoutAdapter
//        tab_layout.setupWithViewPager(view_pager)
//
//
//        val dialogHelper:SubjectDialogHelper= SubjectDialogHelper()
//        val alertView: View = LayoutInflater.from(this).inflate(R.layout.alert_save_subject,null,false)
//        dialogHelper.addSubject(this,alertView,tabLayoutAdapter,view_pager)





    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is AttendanceCriteriaFragment ){
            fragment.setAttendanceListener(this)
        }
    }
}