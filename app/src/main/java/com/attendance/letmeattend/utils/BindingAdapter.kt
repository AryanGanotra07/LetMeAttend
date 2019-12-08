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
import com.attendance.letmeattend.adapters.LectureRecyclerAdapter
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.utils.extentions.getParentActivity
import com.google.android.material.tabs.TabLayout


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
            })

//            id.observe(parentActivity, Observer { value ->
//                val lecturesFilter : List<Lecture> = lectures.value!!.filter { it -> it.day == value }
//                adapter.setLectures(lecturesFilter as ArrayList<Lecture>)
//            })
        }
    }
}
