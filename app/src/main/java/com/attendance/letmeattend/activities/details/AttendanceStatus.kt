package com.attendance.letmeattend.activities.details

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.AttendanceStatusBinding
import com.attendance.letmeattend.databinding.AttendanceStatusResourceBinding
import com.attendance.letmeattend.databinding.TimeTableBinding
import com.attendance.letmeattend.listeners.AttendanceStatusListeners
import com.attendance.letmeattend.models.AttendanceStatusModel
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.utils.toast
import com.google.gson.JsonObject

class AttendanceStatus : Fragment(), AttendanceStatusListeners {
    private lateinit var lecture : LectureModel
    private lateinit var viewModel : AttendanceStatusViewModel
    private lateinit var binding : AttendanceStatusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lecture = arguments!!.getParcelable("lecture")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<AttendanceStatusBinding>(inflater, R.layout.attendance_status, container, false )
        viewModel = ViewModelProviders.of(this).get(AttendanceStatusViewModel::class.java)
        viewModel.attendanceStatusAdapter.setClickListener(this)
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

    override fun onResume() {
        super.onResume()
        if (::lecture.isInitialized && lecture!=null) {
            Handler().postDelayed(Runnable {
                viewModel.getAllAttendanceStatus(lect_id = lecture.id)
            },200)

        }
    }

    override fun onClicked(position: Int, attendanceStatusModel: AttendanceStatusModel) {
        MaterialDialog(activity as EnterDetails).show {
            title(R.string.select)
            listItems(R.array.status) { _, index, text ->
                context?.toast("Selected item $text at index $index")
                val json = JsonObject()
                json.addProperty("id", attendanceStatusModel.id)
                when(index) {
                    0 -> json.addProperty("status", "yes")
                    1 -> json.addProperty("status", "no")
                    2 -> json.addProperty("status", "cancel")

                }
                viewModel.updateAttendanceStatusModel(lect_id = lecture.id, json = json, position = position)
            }

            lifecycleOwner(this@AttendanceStatus)
        }
    }

}