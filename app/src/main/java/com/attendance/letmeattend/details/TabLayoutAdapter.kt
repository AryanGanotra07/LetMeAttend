package com.attendance.letmeattend.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabLayoutAdapter(private var fm: FragmentManager, private var behavior: Int) : FragmentPagerAdapter(fm, behavior) {


    override fun getItem(position: Int): Fragment {

          return Monday()


    }


    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            //0->return "Attendance"
            0-> return "Monday"
            1-> return "Tuesday"
            2-> return "Wednesday"
            3-> return "Thursday"
            4-> return "Friday"
            5-> return "Saturday"
            else->return null
        }
    }




    override fun getCount(): Int {
      return behavior
    }
}