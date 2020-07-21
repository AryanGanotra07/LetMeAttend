package com.attendance.letmeattend.utils

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.attendance.letmeattend.R
import com.attendance.letmeattend.activities.details.DetailsViewModel
import com.attendance.letmeattend.adapters.LectureNewRecyclerAdapter
import com.attendance.letmeattend.adapters.LectureRecyclerAdapter
import com.attendance.letmeattend.adapters.LectureTimeTableAdapter
import com.attendance.letmeattend.adapters.SubjectRecyclerAdapter
import com.attendance.letmeattend.models.*
import com.attendance.letmeattend.utils.extentions.getParentActivity
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.BoomButtons.OnBMClickListener
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton
import com.nightonke.boommenu.BoomMenuButton
import com.ramotion.fluidslider.FluidSlider
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.min


@BindingAdapter("setMutableText")
fun setMutableLectureName(view: TextView, text:MediatorLiveData<String>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer { value ->
            Log.i("TEXTS",value?:"")
            view.text = value ?: "" })
    }

}

@BindingAdapter("setColor")
fun setColor(view: CardView, color:MediatorLiveData<Int>?) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && color != null) {
        color.observe(parentActivity, Observer { value ->
            view.setCardBackgroundColor(value) })
    }

}

@BindingAdapter("setSubjectColor")
fun setSubjectColor(view: CardView, color:Int) {

//    val parentActivity: AppCompatActivity? = view.getParentActivity()
//    if (parentActivity != null && color != null) {
//        color.observe(parentActivity, Observer { value ->
//            view.setCardBackgroundColor(Color.parseColor(value)) })
//    }
    try {
        view.setCardBackgroundColor(color)
    }
    catch (e : Exception) {

    }


}

@BindingAdapter("openTimeTable")
fun openTimeTable(view : Button, fragment : Fragment) {
    val parentActivity : AppCompatActivity? = view.getParentActivity()
    view.setOnClickListener {
        if (parentActivity != null) {
            parentActivity.supportFragmentManager.beginTransaction().add(fragment,"container").commit()
        }
    }
}

@BindingAdapter("getDay")
fun getDay(view : TextView, day : Int) {
    val parentActivity : AppCompatActivity? = view.getParentActivity()
    if (parentActivity!=null) {
        view.setText(parentActivity.resources.getStringArray(R.array.days)[day])
    }
}

@BindingAdapter("startTimer")
fun startTimer(view: TextView, lectureModel: LectureModel) {
    val hour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val minute: Int = Calendar.getInstance().get(Calendar.MINUTE)
    val lectureTime = lectureModel.start_time.split(":")
    val lectureHour = lectureTime[0]
    val lectureMinute = lectureTime[1]
    val millis = (Integer.valueOf(lectureHour) - hour) * 3600
    val lectureT = Calendar.getInstance()
    lectureT.set(Calendar.HOUR_OF_DAY, Integer.valueOf(lectureHour))
    lectureT.set(Calendar.MINUTE, Integer.valueOf(lectureMinute))
    val final = lectureT.timeInMillis - Calendar.getInstance().timeInMillis
    if (final < 0) {
        view.setText("Over")
    } else if (final.equals(0)) {
        view.setText("Now")
    } else {
        val seconds = final / 1000
        if (seconds > 3600) {
            val hours = seconds / 3600
            view.setText(hours.toString() + "h")
        } else if (seconds <= 3600) {
            val minutes = seconds / 60

            view.setText(minutes.toString() + "m")
//        object : CountDownTimer(final, 1000) {
//            override fun onFinish() {
//                view.setText("Now")
//            }
//
//            override fun onTick(millisUntilFinished: Long) {
//                val seconds = millisUntilFinished / 1000
//                if (seconds > 3600) {
//                    val hours = seconds/3600
//                    view.setText(hours.toString()+"h")
//                }
//                else if (seconds <= 3600 ) {
//                    val minutes = seconds/60
//                    if (minutes.rem(10).equals(0)) {
//                        view.setText(minutes.toString() + "m")
//                    }
//                }
//            }
//
//        }.start()
        }
    }
}


@BindingAdapter("setupAdapter")
fun setupAdapter(view: RecyclerView, adapter : LectureRecyclerAdapter)
{
    val parentActivity : AppCompatActivity? = view.getParentActivity()

    if (view!=null && adapter!=null)
    {
        view.layoutManager = LinearLayoutManager(parentActivity,RecyclerView.VERTICAL,false)
        view.adapter = adapter
    }
}

@BindingAdapter("setupSubjectAdapter")
fun setupSubjectAdapter(view: RecyclerView, adapter : SubjectRecyclerAdapter)
{
    val parentActivity : AppCompatActivity? = view.getParentActivity()
    if (view!=null && adapter!=null)
    {

        //view.layoutManager = linearLayoutManager // Add your recycler view to this ZoomRecycler layout
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(view) // Add your recycler view here
//        view.isNestedScrollingEnabled = false

        view.layoutManager = LinearLayoutManager(parentActivity,RecyclerView.HORIZONTAL,false)
        view.adapter = adapter
    }
}

@BindingAdapter("setupLectureAdapter")
fun setupLectureAdapter(view: RecyclerView, adapter : LectureNewRecyclerAdapter)
{
    val parentActivity : AppCompatActivity? = view.getParentActivity()
    if (view!=null && adapter!=null)
    {

        //view.layoutManager = linearLayoutManager // Add your recycler view to this ZoomRecycler layout
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(view) // Add your recycler view here
//        view.isNestedScrollingEnabled = false

        view.layoutManager = LinearLayoutManager(parentActivity,RecyclerView.HORIZONTAL,false)
        view.adapter = adapter
    }
}

@BindingAdapter("setupTTLectureAdapter")
fun setupTTLectureAdapter(view: RecyclerView, lectureAdapter : LectureTimeTableAdapter)
{

    val parentActivity : AppCompatActivity? = view.getParentActivity()
    if (view!=null && lectureAdapter!=null)
    {


        //view.layoutManager = linearLayoutManager // Add your recycler view to this ZoomRecycler layout
//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(view) // Add your recycler view here
//        view.isNestedScrollingEnabled = false

        view.layoutManager = LinearLayoutManager(parentActivity,RecyclerView.HORIZONTAL,false)
        view.adapter = lectureAdapter

//        if (parentActivity != null) {
//
//            lectures.observe(parentActivity, Observer { value ->
//                if (value != null) {
//                    lectureAdapter.setLectures(value)
//                }
//                else
//                {
//                    lectureAdapter.setLectures(ArrayList())
//                }
//            })
//
////            id.observe(parentActivity, Observer { value ->
////                val lecturesFilter : List<Lecture> = lectures.value!!.filter { it -> it.day == value }
////                adapter.setLectures(lecturesFilter as ArrayList<Lecture>)
////            })
//        }
    }
}

@BindingAdapter("lecturesCount")
fun lecturesCount(view : TextView, subject : SubjectModel) {
    view.setText(subject.lectures.size.toString() + " Lectures")
}

@BindingAdapter("attendancePercent")
fun attendancePercent(view: TextView, subject: SubjectModel) {
    if (subject.total_attendance != 0) {
        view.setText(((subject.current_attendance / subject.total_attendance) * 100).toString() + "%")
    }
    else {
        view.setText("100%")
    }
}


@BindingAdapter("updateValue")
fun updateValue(view: FluidSlider, vm : DetailsViewModel)
{
    view.positionListener = {
        val attendancePercent = abs(it*100).toInt()
        vm.attendanceCriteria = attendancePercent
    }
}

@BindingAdapter("setVisibility")
fun setVisibility(view : ProgressBar , progressVisibility : MediatorLiveData<Int>) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null) {
        progressVisibility.observe(parentActivity, object  : Observer<Int> {
            override fun onChanged(t: Int?) {
                if (t!= null) {
                    view.visibility = t!!
                }
                else {
                    view.visibility = View.GONE
                }
            }

        })
    }

}

@BindingAdapter("setBuilders")
fun setBuilders(view : BoomMenuButton, vm: DetailsViewModel) {
    val builder = HamButton.Builder()
        .normalImageRes(R.drawable.ic_exit_to_app_black_24dp)
        .normalTextRes(R.string.logout)
        .pieceColorRes(R.color.quantum_white_100)
        .listener(object  : OnBMClickListener {
            override fun onBoomButtonClick(index: Int) {
                vm.logout()
            }

        })
        .subNormalTextRes(R.string.logout)

    view.addBuilder(builder)
}

@BindingAdapter("setPosition")
fun setPosition(view:FluidSlider, attendanceCriteria : MediatorLiveData<AttendanceCriteria>) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null) {
        attendanceCriteria.observe(parentActivity, object  : Observer<AttendanceCriteria> {
            override fun onChanged(t: AttendanceCriteria?) {
                if (t!= null) {
                    view.position = (t.attendanceCriteria * 0.01).toFloat()
                }
                else {
                    view.position = (0 * 0.01).toFloat()
                }
            }

        })
    }
}

@BindingAdapter("piece_num")
fun setPieceNum(view : BoomMenuButton , subjects : MediatorLiveData<ArrayList<Subject>>)
{
    val parentActivity : AppCompatActivity? = view.getParentActivity()
    if (view != null && subjects!=null)
    {
        if (parentActivity!=null)
        {
            val builder = TextInsideCircleButton.Builder()
                .normalText("ADD NEW")
                .normalColor(parentActivity?.getColor(R.color.colorAccent))
            view.addBuilder(builder)

            subjects.observe(parentActivity!!, Observer {value ->

                for (i in 1 until view.getPiecePlaceEnum().pieceNumber()) {
//                    if (value.size>i-1) {
//                        val builder = TextInsideCircleButton.Builder()
//                            .normalText(value.get(i).name)
//                            .normalColor(value.get(i).color)
//                        view.addBuilder(builder)
//                    } else {
//
//                    }
                    val builder = TextInsideCircleButton.Builder()
                .normalTextRes(R.string.edit_text_error)
                .normalColor(parentActivity!!.getColor(R.color.colorAccent))
                .unable(true)
                .unableColor(parentActivity!!.getColor(R.color.windowBackground))
                    view.addBuilder(builder)
                }
            })
        }

    }
}

@BindingAdapter("adapter","subjects", requireAll = true)
fun updateSubjectsData(view : RecyclerView, adapter : SubjectRecyclerAdapter, subjects: MediatorLiveData<List<SubjectModel>>)
{
    val parentActivity : AppCompatActivity? = view.getParentActivity()
    if (view!=null && adapter!=null && subjects!=null)
    {
        if (parentActivity != null) {

            subjects.observe(parentActivity, Observer { value ->
                if (value != null) {
                    adapter.setSubjects(value)
                }
                else
                {
                    adapter.setSubjects(ArrayList())
                }
            })

//            id.observe(parentActivity, Observer { value ->
//                val lecturesFilter : List<Lecture> = lectures.value!!.filter { it -> it.day == value }
//                adapter.setLectures(lecturesFilter as ArrayList<Lecture>)
//            })
        }
    }
}

@BindingAdapter("adapter","lectures", requireAll = true)
fun updateLecturesData(view : RecyclerView, adapter : LectureNewRecyclerAdapter, lectures: MediatorLiveData<List<LectureModel>>)
{
    val parentActivity : AppCompatActivity? = view.getParentActivity()
    if (view!=null && adapter!=null && lectures!=null)
    {
        if (parentActivity != null) {

            lectures.observe(parentActivity, Observer { value ->
                if (value != null) {
                    adapter.setLectures(value)
                }
                else
                {
                    adapter.setLectures(ArrayList())
                }
            })

//            id.observe(parentActivity, Observer { value ->
//                val lecturesFilter : List<Lecture> = lectures.value!!.filter { it -> it.day == value }
//                adapter.setLectures(lecturesFilter as ArrayList<Lecture>)
//            })
        }
    }
}

@BindingAdapter("lectureAdapter","lectures",requireAll = true)
fun updateLecturesTimeTable(view : RecyclerView, lectureAdapter : LectureTimeTableAdapter,lectures: MediatorLiveData<List<LectureModel>>)
{
    val parentActivity : AppCompatActivity? = view.getParentActivity()
    if (view!=null && lectureAdapter!=null && lectures!=null)
    {
        if (parentActivity != null) {

            lectures.observe(parentActivity, Observer { value ->
                if (value != null) {
                    lectureAdapter.setLectures(value)
                }
                else
                {
                    lectureAdapter.setLectures(ArrayList())
                }
            })

//            id.observe(parentActivity, Observer { value ->
//                val lecturesFilter : List<Lecture> = lectures.value!!.filter { it -> it.day == value }
//                adapter.setLectures(lecturesFilter as ArrayList<Lecture>)
//            })
        }
    }
}


@BindingAdapter("adapter","lectures","id",requireAll = true)
fun updateData(view : RecyclerView, adapter : LectureRecyclerAdapter, lectures : MediatorLiveData<ArrayList<Lecture>>, id : Int)
{
    val parentActivity : AppCompatActivity? = view.getParentActivity()
    if (view!=null && adapter!=null && lectures!=null)
    {
        if (parentActivity != null) {

            lectures.observe(parentActivity, Observer { value ->
                if (value != null) {
                    val lecturesFilter: List<Lecture> = value.filter { it -> it.day == id }
                    adapter.setLectures(lecturesFilter as ArrayList<Lecture>)
                }
                else
                {
                    adapter.setLectures(ArrayList())
                }
            })

//            id.observe(parentActivity, Observer { value ->
//                val lecturesFilter : List<Lecture> = lectures.value!!.filter { it -> it.day == value }
//                adapter.setLectures(lecturesFilter as ArrayList<Lecture>)
//            })
        }
    }
}
