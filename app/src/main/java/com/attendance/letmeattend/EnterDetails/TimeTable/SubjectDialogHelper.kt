package com.attendance.letmeattend.EnterDetails.TimeTable

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.viewpager.widget.ViewPager
import com.attendance.letmeattend.EnterDetails.TabLayoutAdapter
import com.attendance.letmeattend.R
import com.attendance.letmeattend.colorseekbar.ColorSeekBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.alert_save_subject.view.*

class SubjectDialogHelper {



     fun addSubject(activity:Activity,alertView: View,adapter: TabLayoutAdapter,viewPager:ViewPager){

        var subject:EditText=alertView.findViewById(R.id.subject) as EditText
        var save_btn=alertView.findViewById(R.id.save) as Button
        var cancel_btn=alertView.findViewById(R.id.cancel) as Button
       // var change_color_btn=alertView.findViewById(R.id.color_btn) as Button
        var colorSeekBar=alertView.findViewById(R.id.colorSlider) as ColorSeekBar
        var from_time=alertView.findViewById(R.id.start_time) as TextView
        var to_time=alertView.findViewById(R.id.end_time) as TextView
        var add_btn=activity.findViewById(R.id.add_btn) as ImageButton
        var alert:AlertDialog.Builder=AlertDialog.Builder(activity,R.style.MyDialogTheme)
        alert.setTitle(R.string.dialog_title)
        alert.setCancelable(false)
        alert.setView(alertView)
        val dialog:AlertDialog=alert.create()
        add_btn.setOnClickListener { dialog.show() }
        cancel_btn.setOnClickListener { dialog.dismiss() }
        save_btn.setOnClickListener {

            if(TextUtils.isEmpty(subject.getText().toString())){
                subject.error=activity.resources.getString(R.string.edit_text_error)
            }
            else if(!from_time.text.toString().matches(".*\\d+.*".toRegex()) || !to_time.text.toString().matches(".*\\d+.*".toRegex())){
                Snackbar.make(alertView, R.string.time_error,Snackbar.LENGTH_LONG).show()
            }
            else{


            }
        }

        from_time.setOnClickListener {
            val c = java.util.Calendar.getInstance()
            val mHour = c.get(java.util.Calendar.HOUR_OF_DAY)
            val mMinute = c.get(java.util.Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(activity,R.style.TimePickerDialog,
                TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                    from_time.text = String.format("%02d:%02d", hourOfDay, minute)
                }, mHour, mMinute, true
            )
            timePickerDialog.show()
        }

        to_time.setOnClickListener {
            val c=java.util.Calendar.getInstance()
            val mHour = c.get(java.util.Calendar.HOUR_OF_DAY)
            val mMinute = c.get(java.util.Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(activity,R.style.TimePickerDialog,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                to_time.text=String.format("%02d:%02d", hourOfDay,minute)

            },mHour,mMinute,true)

            timePickerDialog.show()
        }

        colorSeekBar.setMaxPosition(100)
        colorSeekBar.setColorSeeds(R.array.material_colors);
        colorSeekBar.colorBarPosition=17
        colorSeekBar.setOnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
           alertView.setBackgroundColor(color)

        }


    }
}