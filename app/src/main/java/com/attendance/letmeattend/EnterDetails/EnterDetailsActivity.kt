package com.attendance.letmeattend.EnterDetails

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.attendance.letmeattend.EnterDetails.TimeTable.SubjectDialogHelper
import com.attendance.letmeattend.R
import com.ramotion.fluidslider.FluidSlider
import kotlinx.android.synthetic.main.enter_details_activity.*
import kotlinx.android.synthetic.main.enter_details_activity.view.*
import java.util.zip.Inflater


class EnterDetailsActivity: AppCompatActivity(), SaveClickListener {

    override fun onSave(attendance: Int) {
        add_btn.visibility = View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.enter_details_activity)

        val attendanceCriteriaFragment:AttendanceCriteriaFragment= AttendanceCriteriaFragment()


        val fragmentManager:FragmentManager=supportFragmentManager
        val fragmentTransaction:FragmentTransaction=fragmentManager.beginTransaction()
       // fragmentTransaction.add(R.id.frame_layout,timeTableFragment)
       // fragmentTransaction.commit()




        val tabLayoutAdapter:TabLayoutAdapter= TabLayoutAdapter(supportFragmentManager,7)
        val view_pager:ViewPager=findViewById(R.id.view_pager) as ViewPager
        view_pager.adapter=tabLayoutAdapter
        tab_layout.setupWithViewPager(view_pager)


        val dialogHelper:SubjectDialogHelper= SubjectDialogHelper()
        val alertView: View = LayoutInflater.from(this).inflate(R.layout.alert_save_subject,null,false)
        dialogHelper.addSubject(this,alertView,tabLayoutAdapter,view_pager)





    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is AttendanceCriteriaFragment ){
            fragment.setAttendanceListener(this)
        }
    }
}