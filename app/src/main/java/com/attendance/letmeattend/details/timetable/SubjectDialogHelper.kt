package com.attendance.letmeattend.details.timetable

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.R
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.attendance.letmeattend.colorseekbar.ColorSeekBar
import com.google.android.material.snackbar.Snackbar

class SubjectDialogHelper(val context : Activity,val alertView: View, val viewModel: EnterDetailsViewModel) {


    private var subject:EditText=alertView.findViewById(R.id.subject) as EditText
    private var save_btn=alertView.findViewById(R.id.save) as Button
    private var cancel_btn=alertView.findViewById(R.id.cancel) as Button
    // var change_color_btn=alertView.findViewById(R.id.color_btn) as Button
    private var colorSeekBar=alertView.findViewById(R.id.colorSlider) as ColorSeekBar
    private var from_time=alertView.findViewById(R.id.start_time) as TextView
    private var to_time=alertView.findViewById(R.id.end_time) as TextView
    // var add_btn=activity.findViewById(R.id.add_btn) as ImageButton
    private var alert:AlertDialog.Builder=AlertDialog.Builder(context,R.style.MyDialogTheme)

    private var dialog: AlertDialog



    init {
        alert.setTitle(R.string.dialog_title)
        alert.setCancelable(false)
        alert.setView(alertView)
        dialog = alert.create()
    }





      fun addSubject(day : Int){


      //  add_btn.setOnClickListener { dialog.show() }

          val lecture : Lecture = Lecture("101",day)



          cancel_btn.setOnClickListener { dialog.dismiss() }
          save_btn.setOnClickListener {

            if(TextUtils.isEmpty(subject.getText().toString())){
                subject.error=context.resources.getString(R.string.edit_text_error)
            }
            else if(!from_time.text.toString().matches(".*\\d+.*".toRegex()) || !to_time.text.toString().matches(".*\\d+.*".toRegex())){
                Snackbar.make(alertView, R.string.time_error,Snackbar.LENGTH_LONG).show()
            }
            else{
                    lecture.name = subject.text.toString()
                lecture.e_time = to_time.text.toString()
                lecture.s_time = from_time.text.toString()
                lecture.color = colorSeekBar.color
                viewModel.addLecture(lecture)
                dialog.dismiss()

            }
        }

        from_time.setOnClickListener {
            val c = java.util.Calendar.getInstance()
            val mHour = c.get(java.util.Calendar.HOUR_OF_DAY)
            val mMinute = c.get(java.util.Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(context,R.style.TimePickerDialog,
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
            val timePickerDialog = TimePickerDialog(context,R.style.TimePickerDialog,TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
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


          dialog.show()



    }
}