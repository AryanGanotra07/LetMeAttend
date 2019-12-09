package com.attendance.letmeattend.utils

import android.graphics.Color
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.attendance.letmeattend.R
import com.attendance.letmeattend.adapters.LectureRecyclerAdapter
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.Subject
import com.attendance.letmeattend.utils.extentions.getParentActivity
import com.google.android.material.tabs.TabLayout
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton
import com.nightonke.boommenu.BoomMenuButton
import com.nightonke.boommenu.Piece.PiecePlaceEnum
import kotlinx.android.synthetic.main.monday.*


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
