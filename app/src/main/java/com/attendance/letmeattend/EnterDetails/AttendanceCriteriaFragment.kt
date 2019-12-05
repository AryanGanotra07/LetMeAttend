package com.attendance.letmeattend.EnterDetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.attendance.letmeattend.EnterDetails.Listeners.SaveClickListener
import com.attendance.letmeattend.R
import com.ramotion.fluidslider.FluidSlider
import kotlinx.android.synthetic.main.attendance_criteria_fragment.*
import kotlin.math.abs

class AttendanceCriteriaFragment(): Fragment() {

    private lateinit var callback : SaveClickListener





    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.attendance_criteria_fragment,container,false)
    }

    fun setAttendanceListener(callback : SaveClickListener){
        this.callback = callback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var attendancePercent:Int
        val slider = view.findViewById<FluidSlider>(R.id.fluidSlider)
        slider.position=0.75f
        slider.positionListener = {
        }

        save_attendance.setOnClickListener {
            attendancePercent= abs(slider.position*100).toInt()
            Log.i("Attendance",attendancePercent.toString())
            callback.onSave(attendancePercent)
        }


    }



}