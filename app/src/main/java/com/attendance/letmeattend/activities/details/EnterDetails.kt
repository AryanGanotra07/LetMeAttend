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
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.color.ColorPalette
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.DetailsActivityBinding
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.internal.proxy.NullProxySelector.select
import okhttp3.internal.toHexString
import org.json.JSONObject
import org.w3c.dom.Text
import java.util.HashMap


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