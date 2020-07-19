package com.attendance.letmeattend.activities.details

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
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
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.datetime.timePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.attendance.letmeattend.R
import com.attendance.letmeattend.adapters.LectureNewRecyclerAdapter
import com.attendance.letmeattend.databinding.DetailsActivityBinding
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.alert_save_subject.*
import okhttp3.internal.proxy.NullProxySelector.select
import okhttp3.internal.toHexString
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.*


class EnterDetails : AppCompatActivity() {
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
            //showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))
        })

        binding.addCourseFb.setOnClickListener {
            showCustomViewDialog(BottomSheet())

        }
    }

    private fun showCustomEditDialog(position: Int,subjectModel: SubjectModel, dialogBehavior: DialogBehavior = ModalDialog) {

            val dialog = MaterialDialog(this, dialogBehavior).show {
                title(R.string.edit_course)

                customView(R.layout.add_course, scrollable = true, horizontalPadding = true)
                val nameET = getCustomView().findViewById<EditText>(R.id.course_name)
                nameET.setText(subjectModel.name)
                positiveButton(R.string.add_course) { dialog ->
                    val json = HashMap<String, Any>()
                    json.put("id", subjectModel.id)
                    json.put("name", nameET.text.toString())
                    json.put("color", subjectModel.color)
                    Log.d(TAG, json.toString())
                    viewModel.updateSubject(position,json)
                }
                val color_button = getCustomView().findViewById<FloatingActionButton>(R.id.color_view)
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

        dialog.getCustomView().setBackgroundColor(subjectModel.color)


    }

    private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {
        val dialog = MaterialDialog(this, dialogBehavior).show {
            title(R.string.add_course)

            customView(R.layout.add_course, scrollable = true, horizontalPadding = true)
            val nameET = getCustomView().findViewById<EditText>(R.id.course_name)
            var my_color : Int = Color.DKGRAY
            positiveButton(R.string.add_course) { dialog ->
                val json = HashMap<String, Any>()
                json.put("name", nameET.text.toString())
                json.put("color", my_color)
                Log.d(TAG, json.toString())
                viewModel.addSubject(json)
            }
            val topLevel = intArrayOf(Color.DKGRAY,Color.parseColor("#62af97"),Color.parseColor("#69b5e1"),Color.parseColor("#00587f"),Color.parseColor("#cfe1af"),Color.parseColor("#d1d8e1"),Color.parseColor("#e1c6b0"),Color.parseColor("#bbe1df"),Color.parseColor("#4a4c7f"),Color.parseColor("#a8a7e1"),Color.parseColor("#e2c09e"),Color.parseColor("#566be2"))
            val color_button = getCustomView().findViewById<FloatingActionButton>(R.id.color_view)
            val recyclerView = getCustomView().findViewById<RecyclerView>(R.id.recyclerView)
            val layoutManager = LinearLayoutManager(this@EnterDetails, RecyclerView.HORIZONTAL, false)
            val adapter = LectureNewRecyclerAdapter()
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
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

                    }
                    positiveButton(R.string.select)
                    negativeButton(android.R.string.cancel)
                    lifecycleOwner(this@EnterDetails)
                }
            }

            val add_lecture = getCustomView().findViewById<FloatingActionButton>(R.id.add_lecture_fb)
            val lecture : LectureModel = LectureModel(-1,1,"","","",my_color)
            add_lecture.setOnClickListener {
                val dialog = MaterialDialog(this@EnterDetails).show {
                    title(R.string.add_lectures)
                    customView(R.layout.add_lecture, scrollable = true, horizontalPadding = true)
                    positiveButton(R.string.add_lectures) { dialog ->
                        adapter.addLecture(lecture)

//                        val json = HashMap<String, Any>()
//                        json.put("name", nameET.text.toString())
//                        json.put("color", my_color)
//                        Log.d(TAG, json.toString())
//                        viewModel.addSubject(json)
                    }
                    negativeButton(android.R.string.cancel)
                    val day_select = getCustomView().findViewById<TextView>(R.id.day_tv)
                    val start_time_selet = getCustomView().findViewById<TextView>(R.id.start_time)
                    val end_time_select = getCustomView().findViewById<TextView>(R.id.end_time)
                    day_select.setOnClickListener {
                        MaterialDialog(this@EnterDetails).show {
                            title(R.string.day)
                            listItemsSingleChoice(R.array.days, initialSelection = 0) { _, index, text ->
                                toast("Selected item $text at index $index")
                                day_select.setText(text)
                                lecture.day = index
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
                                lecture.start_time = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
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
                                lecture.end_time = String.format("%02d:%02d:%02d", hourOfDay,minute,seconds)
                            }
                            lifecycleOwner(this@EnterDetails)
                        }
                    }

                }
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
       // viewModel.refreshData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            onBackPressed()
            Toast.makeText(this, "OnBAckPressed Works", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        Log.d(TAG, item!!.itemId.toString())
        val position = viewModel.subjectRecyclerAdapter.getPosition()
        val subject = viewModel.subjectRecyclerAdapter.getLecture(position)
        if (item.itemId == R.id.delete_lecture) {
            viewModel.onSubjectDelete(subject)
        }
        else if (item.itemId == R.id.edit_lecture) {

            viewModel.onSubjectEdit(subject)
            showCustomEditDialog(position, subject, BottomSheet())
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
}