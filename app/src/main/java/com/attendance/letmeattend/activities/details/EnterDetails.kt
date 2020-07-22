package com.attendance.letmeattend.activities.details

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.color.colorChooser
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onShow
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.datetime.timePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.attendance.letmeattend.R
import com.attendance.letmeattend.adapters.AddLectureRecyclerAdapter
import com.attendance.letmeattend.adapters.LectureNewRecyclerAdapter
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.databinding.DetailsActivityBinding
import com.attendance.letmeattend.listeners.LectureListeners
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.add_course.*
import kotlinx.android.synthetic.main.add_course.recyclerView
import kotlinx.android.synthetic.main.add_lecture.*
import kotlinx.android.synthetic.main.alert_save_subject.*
import kotlinx.android.synthetic.main.details_activity.*
import okhttp3.internal.proxy.NullProxySelector.select
import okhttp3.internal.toHexString
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList


class EnterDetails : AppCompatActivity(), LectureListeners {
    private val TAG = "EnterDetails"
    lateinit var viewModel : DetailsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding: DetailsActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.details_activity)
        setSupportActionBar(binding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        supportActionBar?.setShowHideAnimationEnabled(false)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.vm = viewModel
        val fragmentManager = supportFragmentManager
        viewModel.fragmentDisplayer.observe(this, Observer {
            Log.d(TAG, "Got fragment request")
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit).replace(binding.frame.id, it).addToBackStack(null).commit()
            viewModel.subjectsLiveData.postValue(ArrayList())
            viewModel.lecturesLiveData.postValue(ArrayList())
            //showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))
        })

        binding.addCourseFb.setOnClickListener {
            showCustomViewDialog(BottomSheet())

        }
        empty_view.setOnClickListener {
            showCustomViewDialog(BottomSheet())
        }
    }

    private fun showCustomEditDialog(position: Int,subjectModel: SubjectModel, dialogBehavior: DialogBehavior = ModalDialog) {

            val dialog = MaterialDialog(this, dialogBehavior).show {
                title(R.string.edit_course)

                customView(R.layout.add_course, scrollable = true, horizontalPadding = true)
                val nameET = getCustomView().findViewById<EditText>(R.id.course_name)
                nameET.setText(subjectModel.name)
                positiveButton(R.string.edit_course) { dialog ->
                    val json = HashMap<String, Any>()
                    json.put("id", subjectModel.id)
                    json.put("name", nameET.text.toString())
                    json.put("color", subjectModel.color)
                    Log.d(TAG, json.toString())
                    viewModel.updateSubject(position,json)
                }
                val color_button = getCustomView().findViewById<FloatingActionButton>(R.id.color_view)
                val color_tv = color_choose_tv
                color_tv.visibility = View.GONE
                add_lecture_view.visibility = View.GONE
                recyclerView.visibility = View.GONE
                color_button.setBackgroundTintList(ColorStateList.valueOf(subjectModel.color))
//               color_button.visibility = View.GONE
                color_button.isEnabled = false
                color_button.isClickable = false


//            getCustomView().findViewById<TextView>(R.id.monday_tv).setOnClickListener {
//                Log.d(TAG, viewModel.getDay())
//                viewModel.getUserCourses()
//            }
                negativeButton(android.R.string.cancel)
                lifecycleOwner(this@EnterDetails)
            }

//        dialog.getCustomView().setBackgroundColor(subjectModel.color)


    }
    
    private fun validate(day : Int, s_t: String, e_t : String) : Boolean {

        if (day == -1)  {
            toast("Please choose a day")
            return false
        }
        if (s_t.isNullOrEmpty() || e_t.isNullOrEmpty()) {
            toast("Please select the timings")
            return false
        }
        onResumeFragments()
        return true
    }

    private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {
        val dialog = MaterialDialog(this, dialogBehavior).show {


            title(R.string.add_course)

            customView(R.layout.add_course, scrollable = true, horizontalPadding = true)
            val nameET = getCustomView().findViewById<EditText>(R.id.course_name)
            var my_color : Int = Color.WHITE
            val adapter = AddLectureRecyclerAdapter()
            val lectures = ArrayList<LectureModel>()
            positiveButton(R.string.add_course) { dialog ->
//                val json = HashMap<String, Any>()
//                json.put("name", nameET.text.toString())
//                json.put("color", my_color)
//                Log.d(TAG, json.toString())
//
//                viewModel.addSubject(json)

                val json = JsonObject()
                val subjectJSON = JsonObject()
                subjectJSON.addProperty("name" , nameET.text.toString())
                subjectJSON.addProperty("color", my_color)
                json.add("subject", subjectJSON)
                val jsonArray = JsonArray()
                val lectures = adapter.getLectures()
                if (lectures!= null && lectures.isNotEmpty()) {
                    for (lecture : LectureModel in lectures) {
                        lecture.color = my_color
                        lecture.name = nameET.text.toString()
                        jsonArray.add(lecture.toJSON())
                    }
                }
                json.add("lectures", jsonArray)
                Log.d(TAG, json.toString())
                viewModel.addSubjectWithLectures(json)
            }


            fun showLecture(lectureModel: LectureModel?) {
                var day : Int = 0
                var s_t : String = ""
                var e_t : String = ""
                if (lectureModel!=null) {
                    day = lectureModel.day
                    s_t = lectureModel.start_time
                    e_t = lectureModel.end_time
                }
                val add_lecture_dialog = MaterialDialog(this@EnterDetails)
                add_lecture_dialog.setOnShowListener {
                    onShow {
                        add_lecture_dialog.positiveButton(R.string.add_lectures) { dialog ->
                            if (validate(day, s_t, e_t)) {
                                val lecture = LectureModel(
                                    Random().nextInt(),
                                    day,
                                    s_t,
                                    e_t,
                                    nameET.text.toString(),
                                    Color.WHITE
                                )
                                //lectures.add(lecture)
                                if (lectureModel!=null) {
                                    adapter.setLecture(adapter.getPosition(), lecture)
                                }
                                else {
                                    adapter.addLecture(lecture)
                                }
                                add_lecture_dialog.dismiss()
                            }
                            day = 0
                            s_t = ""
                            e_t = ""


//                        val json = HashMap<String, Any>()
//                        json.put("name", nameET.text.toString())
//                        json.put("color", my_color)
//                        Log.d(TAG, json.toString())
//                        viewModel.addSubject(json)
                        }
                    }

                }
                    add_lecture_dialog.show {
                    title(R.string.add_lectures)
                    customView(R.layout.add_lecture, scrollable = true, horizontalPadding = true)

                    negativeButton(android.R.string.cancel)
                    val day_select = getCustomView().findViewById<TextView>(R.id.day_tv)

                    val start_time_selet = getCustomView().findViewById<TextView>(R.id.start_time)
                    val end_time_select = getCustomView().findViewById<TextView>(R.id.end_time)
                    if (lectureModel!=null) {
                        day_select.setText(resources.getStringArray(R.array.days)[day])
                        start_time_selet.setText(lectureModel.start_time)
                        end_time_select.setText(lectureModel.end_time)
                    }
                    day_select.setOnClickListener {
                        MaterialDialog(this@EnterDetails).show {
                            title(R.string.day)
                            listItemsSingleChoice(R.array.days, initialSelection = day) { _, index, text ->
                                toast("Selected item $text at index $index")
                                day_select.setText(text)
                                day = index
                            }
                            lifecycleOwner(this@EnterDetails)
                        }
                    }
                    start_time_selet.setOnClickListener {
                        MaterialDialog(this@EnterDetails).show {
                            title(R.string.dialog_select_time)
                            timePicker { _, time ->
                                val hourOfDay = time.get(Calendar.HOUR_OF_DAY)
                                val minute = time.get(Calendar.MINUTE)
                                val seconds = time.get(Calendar.SECOND)
                                start_time_selet.setText(String.format("%02d:%02d:%02d", hourOfDay,minute,seconds))
                                s_t = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
                            }

                            lifecycleOwner(this@EnterDetails)
                        }
                    }
                    end_time_select.setOnClickListener {
                        MaterialDialog(this@EnterDetails).show {
                            title(R.string.choose_time)
                            timePicker { _, time ->
                                val hourOfDay = time.get(Calendar.HOUR_OF_DAY)
                                val minute = time.get(Calendar.MINUTE)
                                val seconds = time.get(Calendar.SECOND)
                                end_time_select.setText(String.format("%02d:%02d:%02d", hourOfDay,minute,seconds))
                                e_t = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
                            }
                            lifecycleOwner(this@EnterDetails)
                        }
                    }
                    cancelOnTouchOutside(false)
                    cancelable(false)


                }

            }

            val topLevel = intArrayOf(Color.WHITE,Color.DKGRAY,Color.parseColor("#62af97"),Color.parseColor("#69b5e1"),Color.parseColor("#00587f"),Color.parseColor("#cfe1af"),Color.parseColor("#d1d8e1"),Color.parseColor("#e1c6b0"),Color.parseColor("#bbe1df"),Color.parseColor("#4a4c7f"),Color.parseColor("#a8a7e1"),Color.parseColor("#e2c09e"),Color.parseColor("#566be2"))
            val color_button = getCustomView().findViewById<FloatingActionButton>(R.id.color_view)
            val recyclerView = getCustomView().findViewById<RecyclerView>(R.id.recyclerView)
            val layoutManager = LinearLayoutManager(this@EnterDetails, RecyclerView.VERTICAL, false)

            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            adapter.setLectures(lectures)
            val lectureListeners : LectureListeners = object : LectureListeners {

                override fun onLectureEdit(position: Int, lecture: LectureModel) {
                    showLecture(lecture)
                }

                override fun onLectureDelete(lecture: LectureModel) {
                    adapter.removeLecture(adapter.getPosition())
                }

                override fun onLectureClick(lecture: LectureModel) {
                    TODO("Not yet implemented")
                }

            }
            adapter.setClickListener(lectureListeners)

            color_button.setBackgroundTintList(ColorStateList.valueOf(topLevel.get(0)))
            color_button.setOnClickListener {
                MaterialDialog(this@EnterDetails).show {
                    title(R.string.choose_color)
                    colorChooser(topLevel)
                    { _, color ->
                        toast("Selected color: ${color}")
                        my_color = color
                        color_button.setBackgroundColor(Color.BLACK)
                        color_button.setBackgroundTintList(ColorStateList.valueOf(color))
//                        adapter.getLectures().forEach { lm -> lm.color = color }
//                        adapter.notifyDataSetChanged()

                    }
                    positiveButton(R.string.select)
                    negativeButton(android.R.string.cancel)
                    lifecycleOwner(this@EnterDetails)
                }
            }




            val add_lecture = getCustomView().findViewById<FloatingActionButton>(R.id.add_lecture_fb)




            add_lecture.setOnClickListener {
                showLecture(null)
            }

//            getCustomView().findViewById<TextView>(R.id.monday_tv).setOnClickListener {
//                Log.d(TAG, viewModel.getDay())
//                viewModel.getUserCourses()
//            }
            negativeButton(android.R.string.cancel)
            lifecycleOwner(this@EnterDetails)
        }



    }

    fun getSubjects() {

    }

    override fun onResume() {
        super.onResume()
//        supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        supportActionBar?.setShowHideAnimationEnabled(false)
        Handler().post(Runnable { viewModel.refreshData()  })

    }

    fun refresh() {
        viewModel.refreshData()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            onBackPressed()
            Toast.makeText(this, "OnBAckPressed Works", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteConfirmationDialog(subject: SubjectModel) {
        MaterialDialog(this).show {
            message(R.string.delete_confirmation)
            positiveButton(R.string.delete) {
                viewModel.onSubjectDelete(subject)
            }
            negativeButton(android.R.string.cancel)
            lifecycleOwner(this@EnterDetails)
        }
    }

    private fun showEditLectureDialog(position: Int,lectureModel: LectureModel) {
        MaterialDialog(this@EnterDetails, BottomSheet()).show {
            title(R.string.edit_lecture)
            customView(R.layout.add_lecture, scrollable = true, horizontalPadding = true)
            day_view.visibility = View.GONE

            positiveButton(R.string.edit_lecture) {
                toast("Updated lecture")
                val jsonObject = JsonObject()
                jsonObject.addProperty("id", lectureModel.id)
                jsonObject.addProperty("start_time", lectureModel.start_time)
                jsonObject.addProperty("end_time", lectureModel.end_time)
                viewModel.editLecture(position, jsonObject)
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
                MaterialDialog(this@EnterDetails).show {
                    title(R.string.day)
                    listItemsSingleChoice(R.array.days, initialSelection = lectureModel.day) { _, index, text ->
                        toast("Selected item $text at index $index")
                        day_select.setText(text)
                        lectureModel.day = index
                    }
                    lifecycleOwner(this@EnterDetails)
                }
            }
            start_time_selet.setOnClickListener {
                MaterialDialog(this@EnterDetails).show {
                    title(R.string.dialog_select_time)
                    timePicker { _, time ->
                        val hourOfDay = time.get(Calendar.HOUR_OF_DAY)
                        val minute = time.get(Calendar.MINUTE)
                        val seconds = time.get(Calendar.SECOND)
                        start_time_selet.setText(String.format("%02d:%02d:%02d", hourOfDay,minute,seconds))
                        lectureModel.start_time = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
                    }

                    lifecycleOwner(this@EnterDetails)
                }
            }
            end_time_select.setOnClickListener {
                MaterialDialog(this@EnterDetails).show {
                    title(R.string.choose_time)
                    timePicker { _, time ->
                        val hourOfDay = time.get(Calendar.HOUR_OF_DAY)
                        val minute = time.get(Calendar.MINUTE)
                        val seconds = time.get(Calendar.SECOND)
                        end_time_select.setText(String.format("%02d:%02d:%02d", hourOfDay,minute,seconds))
                        lectureModel.end_time = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
                    }
                    lifecycleOwner(this@EnterDetails)
                }
            }
            cancelOnTouchOutside(false)
            cancelable(false)


        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        Log.d(TAG, item!!.itemId.toString())

        if (item.itemId == R.id.delete_lecture) {
            val position = viewModel.lectureRecyclerAdapter.getPosition()
            val lecture = viewModel.lectureRecyclerAdapter.getLecture(position)
            viewModel.deleteLecture(lecture)
        }
        else if (item.itemId == R.id.edit_lecture) {
            val position = viewModel.lectureRecyclerAdapter.getPosition()
            val lecture = viewModel.lectureRecyclerAdapter.getLecture(position)
           showEditLectureDialog(position, lecture)
        }
        else if (item.itemId == R.id.edit_subject) {
            val position = viewModel.subjectRecyclerAdapter.getPosition()
            val subject = viewModel.subjectRecyclerAdapter.getLecture(position)
            viewModel.onSubjectEdit(subject)
            showCustomEditDialog(position, subject, BottomSheet())
        }
        else if (item.itemId == R.id.delete_subject) {
            val position = viewModel.subjectRecyclerAdapter.getPosition()
            val subject = viewModel.subjectRecyclerAdapter.getLecture(position)
            showDeleteConfirmationDialog(subject)
        }
        return super.onContextItemSelected(item)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onLectureEdit(position: Int, lecture: LectureModel) {
        TODO("Not yet implemented")
    }

    override fun onLectureDelete(lecture: LectureModel) {
        TODO("Not yet implemented")
    }

    override fun onLectureClick(lecture: LectureModel) {
        val attendanceStatus = AttendanceStatus()
        val bundle = Bundle()
        bundle.putParcelable("lecture", lecture)
        attendanceStatus.arguments = bundle
        supportFragmentManager!!.beginTransaction().setCustomAnimations(R.anim.design_bottom_sheet_slide_in, R.anim.design_bottom_sheet_slide_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit).add(R.id.frame, attendanceStatus).addToBackStack(null).commit()
    }
}