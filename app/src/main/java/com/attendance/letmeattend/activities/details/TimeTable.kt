package com.attendance.letmeattend.activities.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.datetime.timePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.attendance.letmeattend.R
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.databinding.TimeTableBinding
import com.attendance.letmeattend.listeners.LectureListeners
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.utils.toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.add_lecture.*
import kotlinx.android.synthetic.main.time_table.*
import kotlinx.android.synthetic.main.time_table.view.*
import java.util.*

class TimeTable : Fragment(), LectureListeners {

    private lateinit var viewModel: TimeTableViewModel
    private val TAG = "TimeTable"
    private lateinit var subject : SubjectModel

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        (context as AppCompatActivity)?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TimeTableViewModel::class.java)
        setListeners()
        val bundle = arguments
        if (bundle!=null) {
            subject = bundle!!.getParcelable("subject")
        }

        Log.d(TAG, "Launchded Time Table")
    }

    private fun setListeners() {
        viewModel.mondayLectureRecyclerAdapter.setClickListener(this)
        viewModel.tuesdayLectureRecyclerAdapter.setClickListener(this)
        viewModel.wednesdayLectureRecyclerAdapter.setClickListener(this)
        viewModel.thursdayLectureRecyclerAdapter.setClickListener(this)
        viewModel.fridayLectureRecyclerAdapter.setClickListener(this)
        viewModel.saturdayLectureRecyclerAdapter.setClickListener(this)
        viewModel.sundayLectureRecyclerAdapter.setClickListener(this)
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

        binding.addLectureFb.setOnClickListener {
            if (subject!=null) {
                addLectureDialog()
            }
        }



        binding.vm = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //(activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    private fun addLectureDialog(dialogBehavior: DialogBehavior = ModalDialog) {

        val dialog = MaterialDialog(this.context!!, dialogBehavior).show {
            title(R.string.edit_course)

            customView(R.layout.alert_save_subject, scrollable = true, horizontalPadding = true)
            //nameET.setText(subjectModel.name)
            positiveButton(R.string.add_course) { dialog ->

            }


//            getCustomView().findViewById<TextView>(R.id.monday_tv).setOnClickListener {
//                Log.d(TAG, viewModel.getDay())
//                viewModel.getUserCourses()
//            }
            negativeButton(android.R.string.cancel)
            lifecycleOwner(this@TimeTable)
        }



    }

    private fun validate(day : Int, s_t: String, e_t : String) : Boolean {

        if (day == -1)  {
            context!!.toast("Please choose a day")
            return false
        }
        if (s_t.isNullOrEmpty() || e_t.isNullOrEmpty()) {
            context!!.toast("Please select the timings")
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        if (activity!=null) {
            (activity as EnterDetails).refresh()
        }
    }

    private fun showEditLectureDialog(position: Int,lectureModel: LectureModel) {
        MaterialDialog(context!!, BottomSheet()).show {
            title(R.string.edit_lecture)
            customView(R.layout.add_lecture, scrollable = true, horizontalPadding = true)
            day_view.visibility = View.GONE

            positiveButton(R.string.edit_lecture) {
                context!!.toast("Updated lecture")
                val jsonObject = JsonObject()
                jsonObject.addProperty("id", lectureModel.id)
                jsonObject.addProperty("start_time", lectureModel.start_time)
                jsonObject.addProperty("end_time", lectureModel.end_time)
                viewModel.editLecture(position,jsonObject)
                //viewModel.editLecture(position, jsonObject)
            }

            negativeButton(android.R.string.cancel)
            val day_select = getCustomView().findViewById<TextView>(R.id.day_tv)

            val start_time_selet = getCustomView().findViewById<TextView>(R.id.start_time)
            val end_time_select = getCustomView().findViewById<TextView>(R.id.end_time)
            if (lectureModel!=null) {
                day_select.setText(resources.getStringArray(R.array.days)[lectureModel.day])
                start_time_selet.setText(lectureModel.start_time)
                end_time_select.setText(lectureModel.end_time)
            }
            day_select.setOnClickListener {
                MaterialDialog(context!!).show {
                    title(R.string.day)
                    listItemsSingleChoice(R.array.days, initialSelection = lectureModel.day) { _, index, text ->
                        context!!.toast("Selected item $text at index $index")
                        day_select.setText(text)
                        lectureModel.day = index
                    }
                    lifecycleOwner(this@TimeTable)
                }
            }
            start_time_selet.setOnClickListener {
                MaterialDialog(context!!).show {
                    title(R.string.dialog_select_time)
                    timePicker { _, time ->
                        val hourOfDay = time.get(Calendar.HOUR_OF_DAY)
                        val minute = time.get(Calendar.MINUTE)
                        val seconds = time.get(Calendar.SECOND)
                        start_time_selet.setText(String.format("%02d:%02d:%02d", hourOfDay,minute,seconds))
                        lectureModel.start_time = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
                    }

                    lifecycleOwner(this@TimeTable)
                }
            }
            end_time_select.setOnClickListener {
                MaterialDialog(context!!).show {
                    title(R.string.choose_time)
                    timePicker { _, time ->
                        val hourOfDay = time.get(Calendar.HOUR_OF_DAY)
                        val minute = time.get(Calendar.MINUTE)
                        val seconds = time.get(Calendar.SECOND)
                        end_time_select.setText(String.format("%02d:%02d:%02d", hourOfDay,minute,seconds))
                        lectureModel.end_time = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
                    }
                    lifecycleOwner(this@TimeTable)
                }
            }



        }
    }


    private fun addLectureDialog() {
        val lectureModel = LectureModel(-1, 0, "","",subject.name,subject.color)
        MaterialDialog(this.context!!, BottomSheet()).show {
            title(R.string.add_lectures)
            customView(R.layout.add_lecture, scrollable = true, horizontalPadding = true)

            positiveButton(R.string.add_lectures) {
                if (validate(lectureModel.day, lectureModel.start_time, lectureModel.end_time)) {
                    viewModel.addLectureBySubject(lectureModel,subject.id)
                }
//                toast("Added lecture")
//                val jsonObject = JsonObject()
//                jsonObject.addProperty("id", lectureModel.id)
//                jsonObject.addProperty("start_time", lectureModel.start_time)
//                jsonObject.addProperty("end_time", lectureModel.end_time)
//                viewModel.editLecture(position, jsonObject)
            }

            negativeButton(android.R.string.cancel)
            val day_select = getCustomView().findViewById<TextView>(R.id.day_tv)

            val start_time_selet = getCustomView().findViewById<TextView>(R.id.start_time)
            val end_time_select = getCustomView().findViewById<TextView>(R.id.end_time)

            day_select.setOnClickListener {
                MaterialDialog(this.context!!).show {
                    title(R.string.day)
                    listItemsSingleChoice(R.array.days, initialSelection = lectureModel.day) { _, index, text ->
//                        toast("Selected item $text at index $index")
                        day_select.setText(text)
                        lectureModel.day = index
                    }
                    lifecycleOwner(this@TimeTable)
                }
            }
            start_time_selet.setOnClickListener {
                MaterialDialog(this.context!!).show {
                    title(R.string.dialog_select_time)
                    timePicker { _, time ->
                        val hourOfDay = time.get(Calendar.HOUR_OF_DAY)
                        val minute = time.get(Calendar.MINUTE)
                        val seconds = time.get(Calendar.SECOND)
                        start_time_selet.setText(String.format("%02d:%02d:%02d", hourOfDay,minute,seconds))
                        lectureModel.start_time = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
                    }

                    lifecycleOwner(this@TimeTable)
                }
            }
            end_time_select.setOnClickListener {
                MaterialDialog(this.context!!).show {
                    title(R.string.choose_time)
                    timePicker { _, time ->
                        val hourOfDay = time.get(Calendar.HOUR_OF_DAY)
                        val minute = time.get(Calendar.MINUTE)
                        val seconds = time.get(Calendar.SECOND)
                        end_time_select.setText(String.format("%02d:%02d:%02d", hourOfDay,minute,seconds))
                        lectureModel.end_time = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
                    }
                    lifecycleOwner(this@TimeTable)
                }
            }
            cancelOnTouchOutside(false)
            cancelable(false)


        }
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

    override fun onLectureClick(lecture: LectureModel) {
        val attendanceStatus = AttendanceStatus()
        val bundle = Bundle()
        bundle.putParcelable("lecture", lecture)
        attendanceStatus.arguments = bundle
        fragmentManager!!.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit).add(R.id.frame, attendanceStatus).addToBackStack(null).commit()
    }

    override fun onLectureEdit(position: Int, lecture: LectureModel) {
        showEditLectureDialog(position,lecture)
    }

    override fun onLectureDelete(lecture: LectureModel) {
        viewModel.deleteLecture(lecture)
    }
}