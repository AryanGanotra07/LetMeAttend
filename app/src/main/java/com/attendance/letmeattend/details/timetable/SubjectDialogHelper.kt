package com.attendance.letmeattend.details.timetable

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.R
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.attendance.letmeattend.colorseekbar.ColorSeekBar
import com.attendance.letmeattend.models.Subject
import com.attendance.letmeattend.utils.toast
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.google.android.material.snackbar.Snackbar

class SubjectDialogHelper(val context : Activity,val alertView: View, val viewModel: EnterDetailsViewModel) {

    private var subject:AutoCompleteTextView=alertView.findViewById(R.id.subject) as AutoCompleteTextView
    private var save_btn=alertView.findViewById(R.id.save) as Button
    private var cancel_btn=alertView.findViewById(R.id.cancel) as Button
    // var change_color_btn=alertView.findViewById(R.id.color_btn) as Button
    private var colorSeekBar=alertView.findViewById(R.id.colorSlider) as ColorSeekBar
    private var from_time=alertView.findViewById(R.id.start_time) as TextView
    private var to_time=alertView.findViewById(R.id.end_time) as TextView
    // var add_btn=activity.findViewById(R.id.add_btn) as ImageButton
    private var alert:AlertDialog.Builder=AlertDialog.Builder(context,R.style.MyDialogTheme)
    private var dialog: AlertDialog
    private var results : List<Subject>? = ArrayList<Subject>()
    val adapter:ArrayAdapter<Subject> = ArrayAdapter(context, R.layout.textview_custom,ArrayList<Subject>())


    init {
        subject.setAdapter(adapter)
        alert.setTitle(R.string.dialog_title)
        alert.setCancelable(false)
        alert.setView(alertView)
        dialog = alert.create()
        subject.addTextChangedListener {
            context.toast(it.toString())
            adapter.clear()
            results = viewModel.getSubjectByName(it.toString())
            if (!results.isNullOrEmpty())
            {
                adapter.addAll(results)
            }
        }

       subject.setOnItemClickListener { parent, view, position, id ->
           val mSubject = parent.adapter.getItem(position) as Subject
           subject.setText(mSubject.name)
           alertView.setBackgroundColor(mSubject.color)
           colorSeekBar.color = mSubject.color

       }


    }

    fun updateSubject( lecture : Lecture)
    {

        subject.setText(lecture.name)
        subject.isEnabled = false
        from_time.setText(lecture.s_time)
        to_time.setText(lecture.e_time)
        colorSeekBar.color = lecture.color
        colorSeekBar.isEnabled = false
        cancel_btn.setOnClickListener { dialog.dismiss() }
        save_btn.setOnClickListener {

            if(TextUtils.isEmpty(subject.getText().toString())){
                subject.error=context.resources.getString(R.string.edit_text_error)
            }
            else if(!from_time.text.toString().matches(".*\\d+.*".toRegex()) || !to_time.text.toString().matches(".*\\d+.*".toRegex())){
                Snackbar.make(alertView, R.string.time_error,Snackbar.LENGTH_LONG).show()
            }
            else{
                //lecture.name = subject.text.toString()
                lecture.e_time = to_time.text.toString()
                lecture.s_time = from_time.text.toString()
                //lecture.color = colorSeekBar.color
                viewModel.updateLecture(lecture)
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
        alertView.setBackgroundColor(lecture.color)


        dialog.show()

    }





      fun addSubject(day : Int, subj: Subject){


      //  add_btn.setOnClickListener { dialog.show() }

          val lecture : Lecture = Lecture("101",day)

            subject.isEnabled = false
          colorSeekBar.isEnabled = false
          colorSeekBar.color = subj.color
          subject.setText(subj.name)
          from_time.setText(R.string.dialog_select_time)
          to_time.setText(R.string.dialog_select_date)
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
                lecture.color = subj.color
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
          alertView.setBackgroundColor(subj.color)
          colorSeekBar.color = subj.color


          dialog.show()



    }
    fun addSubject(day : Int){


        //  add_btn.setOnClickListener { dialog.show() }

        val lecture : Lecture = Lecture("101",day)

        subject.isEnabled = true
        colorSeekBar.isEnabled = true
        subject.setText("")
        from_time.setText(R.string.dialog_select_time)
        to_time.setText(R.string.dialog_select_date)
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

    fun executeDialog()  {
        val builder :  CFAlertDialog.Builder  = CFAlertDialog.Builder(context)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
            .setTitle("You've hit the limit")
            .setMessage("Looks like you've hit your usage limit. Upgrade to our paid plan to continue without any limits.")
//                     .addButton("UPGRADE", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END,object : DialogInterface.OnClickListener {
//                         override fun onClick(dialog: DialogInterface?, which: Int) {
//                             context?.toast("Clicked");
//                         }
//                     })

        builder.show();
    }
}