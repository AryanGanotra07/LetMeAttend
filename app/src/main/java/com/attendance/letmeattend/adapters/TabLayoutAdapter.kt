package com.attendance.letmeattend.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.attendance.letmeattend.details.Monday
import com.attendance.letmeattend.details.timetable.*
import java.util.*

class TabLayoutAdapter(private var fm: FragmentManager, private var behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    var id : Int = 0

    override fun getItem(position: Int): Fragment {

        when(position){
            //0->return "Attendance"
            0-> {
                val day = Monday()
                val bundle = Bundle()
                bundle.putInt("index",Calendar.MONDAY)
                day.arguments = bundle
                return Monday()
            }
            1-> {
                val day = Monday()
                val bundle = Bundle()
                bundle.putInt("index",Calendar.TUESDAY)
                day.arguments = bundle
                return Tuesday()
            }
            2-> {
                val day = Monday()
                val bundle = Bundle()
                bundle.putInt("index",Calendar.WEDNESDAY)
                day.arguments = bundle
                return Wednesday()
            }
            3-> {
                val day = Monday()
                val bundle = Bundle()
                bundle.putInt("index",Calendar.THURSDAY)
                day.arguments = bundle
                return Thursday()
            }
            4-> {
                val day = Monday()
                val bundle = Bundle()
                bundle.putInt("index",Calendar.FRIDAY)
                day.arguments = bundle
                return Friday()
            }
            5-> {
                val day = Monday()
                val bundle = Bundle()
                bundle.putInt("index",Calendar.SATURDAY)
                day.arguments = bundle
                return Saturday()
            }

        }

        return Monday()


    }

    fun getCurrentItem() : Int {
        return id
    }


    override fun getPageTitle(position: Int): CharSequence? {
        id = position
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