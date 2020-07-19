package com.attendance.letmeattend.activities.details

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.TimeTableBinding
import com.attendance.letmeattend.models.SubjectModel
import kotlinx.android.synthetic.main.time_table.*
import kotlinx.android.synthetic.main.time_table.view.*

class TimeTable : Fragment() {

    private lateinit var viewModel: TimeTableViewModel
    private val TAG = "TimeTable"
    private lateinit var subject : SubjectModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TimeTableViewModel::class.java)
        val bundle = arguments
        if (bundle!=null) {
            subject = bundle!!.getParcelable("subject")
        }

        Log.d(TAG, "Launchded Time Table")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<TimeTableBinding>(inflater, R.layout.time_table, container, false )

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowHomeEnabled(true);
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (::subject.isInitialized && subject!=null) {
            Log.d(TAG, subject.name)
            binding.appBar.setBackgroundColor(subject.color)
            binding.appBar.setBackgroundTintList(ColorStateList.valueOf(subject.color))
            binding.titleToolbar.setText(subject.name)
        }



        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed(Runnable {
            if (::subject.isInitialized && subject!=null ) {

                viewModel.getLecturesBySubjects(subject.id)
            }
            else {
                viewModel.getAllLectures()
            }

        },200)

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu?.clear()

        //
        //  inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)


    }
}