package com.attendance.letmeattend.activities.details

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.AttendanceStatusBinding
import com.attendance.letmeattend.databinding.TimeTableBinding
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.LectureModel

class AttendanceStatus : Fragment() {
    private lateinit var lecture : LectureModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lecture = arguments!!.getParcelable("lecture")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<AttendanceStatusBinding>(inflater, R.layout.attendance_status, container, false )
        val viewModel : AttendanceStatusViewModel = ViewModelProviders.of(this).get(AttendanceStatusViewModel::class.java)
        binding.vm = viewModel
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowHomeEnabled(true);
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (::lecture.isInitialized && lecture!=null) {
            binding.appBar.setBackgroundColor(lecture.color)
            binding.appBar.setBackgroundTintList(ColorStateList.valueOf(lecture.color))
            binding.titleToolbar.setText(lecture.name)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}