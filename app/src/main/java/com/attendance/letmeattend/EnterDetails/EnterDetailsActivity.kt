package com.attendance.letmeattend.EnterDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.attendance.letmeattend.EnterDetails.Listeners.AddSubjectListener
import com.attendance.letmeattend.EnterDetails.Listeners.SaveClickListener
import com.attendance.letmeattend.EnterDetails.TimeTable.SubjectDialogHelper
import com.attendance.letmeattend.Model.Attendance
import com.attendance.letmeattend.R
import com.attendance.letmeattend.Utils.toast
import com.attendance.letmeattend.ViewModels.EnterDetailsViewModel
import com.attendance.letmeattend.databinding.EnterDetailsActivityBinding
import kotlinx.android.synthetic.main.enter_details_activity.*


class EnterDetailsActivity: AppCompatActivity(),
    SaveClickListener, AddSubjectListener {

    val fragmentManager:FragmentManager=supportFragmentManager
    val attendanceCriteriaFragment:AttendanceCriteriaFragment= AttendanceCriteriaFragment()

    private lateinit var viewModel : EnterDetailsViewModel
    private lateinit var viewPager: ViewPager
    private lateinit var alertView : View
    private lateinit var dialogHelper : SubjectDialogHelper

    val tabLayoutAdapter:TabLayoutAdapter= TabLayoutAdapter(fragmentManager,6)


    override fun onSave(attendance: Int) {
      //  add_btn.visibility = View.VISIBLE

        //viewModel.setAttendance(Attendance(attendance = attendance))

        val fragment : Fragment? = fragmentManager.findFragmentByTag("attendance");
        fragment?.let { fragmentManager.beginTransaction().remove(it).commit() }
        viewPager.adapter=tabLayoutAdapter
        tab_layout.setupWithViewPager(viewPager)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : EnterDetailsActivityBinding = DataBindingUtil.setContentView(this, R.layout.enter_details_activity)

        viewModel = ViewModelProviders.of(this).get(EnterDetailsViewModel::class.java)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        viewPager = binding.viewPager
        alertView = LayoutInflater.from(this).inflate(R.layout.alert_save_subject,null,false)
        dialogHelper = SubjectDialogHelper(this@EnterDetailsActivity,alertView,viewModel)



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





    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is AttendanceCriteriaFragment ){
            fragment.setAttendanceListener(this)
        }
       else if (fragment is Monday ){
            fragment.setAddSubjectListener(this)
        }
    }

    override fun onAddSubject(day: Int) {
        //applicationContext.toast("Called")
       dialogHelper.addSubject(day)

    }


}