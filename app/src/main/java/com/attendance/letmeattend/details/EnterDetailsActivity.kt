package com.attendance.letmeattend.details

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
import com.attendance.letmeattend.details.listeners.AddSubjectListener
import com.attendance.letmeattend.details.listeners.SaveClickListener
import com.attendance.letmeattend.R
import com.attendance.letmeattend.adapters.TabLayoutAdapter
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.attendance.letmeattend.databinding.EnterDetailsActivityBinding
import com.attendance.letmeattend.details.listeners.OnLectureClickListener
import com.attendance.letmeattend.details.timetable.*
import com.attendance.letmeattend.models.Attendance
import com.attendance.letmeattend.models.Lecture
import com.google.android.material.tabs.TabLayout


class EnterDetailsActivity: AppCompatActivity(),
    SaveClickListener, AddSubjectListener, OnLectureClickListener {

    val fragmentManager:FragmentManager=supportFragmentManager
    val attendanceCriteriaFragment:AttendanceCriteriaFragment= AttendanceCriteriaFragment()

    private lateinit var viewModel : EnterDetailsViewModel
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout : TabLayout
    private lateinit var alertView : View
    private lateinit var dialogHelper : SubjectDialogHelper
    private var id : Int = 0

    val tabLayoutAdapter: TabLayoutAdapter =
        TabLayoutAdapter(fragmentManager, 6)


    override fun onSave(attendance: Int) {
      //  add_btn.visibility = View.VISIBLE

        viewModel.setAttendance(Attendance(attendance = attendance))

        val fragment : Fragment? = fragmentManager.findFragmentByTag("attendance");
        fragment?.let { fragmentManager.beginTransaction().remove(it).commit() }
        viewPager.adapter=tabLayoutAdapter
        viewPager.currentItem = 0
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : EnterDetailsActivityBinding = DataBindingUtil.setContentView(this, R.layout.enter_details_activity)

        viewModel = ViewModelProviders.of(this).get(EnterDetailsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout
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

//        viewPager.addOnPageChangeListener(onPageChangeListener)
//        viewPager.post(Runnable {
//            onPageChangeListener.onPageSelected(viewPager.currentItem)
//        })

//
//        val tabLayoutAdapter:TabLayoutAdapter= TabLayoutAdapter(supportFragmentManager,7)
//        val view_pager:ViewPager=findViewById(R.id.view_pager) as ViewPager
//        view_pager.adapter=tabLayoutAdapter
//        tab_layout.setupWithViewPager(view_pager)
//

//
    }

    val onPageChangeListener : ViewPager.OnPageChangeListener = object  : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
           // this@EnterDetailsActivity.toast(positionOffsetPixels.toString())

        }

        override fun onPageSelected(position: Int) {
            id = position
        }

    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is AttendanceCriteriaFragment ){
            fragment.setAttendanceListener(this)

        }
       else if (fragment is Monday ){
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Tuesday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Wednesday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Thursday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Friday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
        else if (fragment is Saturday)
        {
            fragment.setAddSubjectListener(this)
            fragment.setOnLectureClickListener(this)
        }
    }

    override fun onAddSubject(day: Int) {
        //applicationContext.toast("Called")
       dialogHelper.addSubject(day)

    }

    fun getFragmentId(): Int {
       return viewPager.currentItem


    }

    override fun onLectureClick(lecture: Lecture) {
           dialogHelper.updateSubject(lecture)
    }


}