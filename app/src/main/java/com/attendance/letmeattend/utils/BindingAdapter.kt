package com.attendance.letmeattend.utils

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.attendance.letmeattend.adapters.LectureRecyclerAdapter
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.utils.extentions.getParentActivity
import com.google.android.material.tabs.TabLayout


@BindingAdapter("mutableList")
fun setMutableTest(view: RecyclerView, lectures : MediatorLiveData<ArrayList<Lecture>>) {

    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && lectures != null){
       lectures.observe(parentActivity, Observer { value -> view.adapter?.setLectures(value) })
    }


}

@BindingAdapter("setupAdapter")
fun setAdapter(view: RecyclerView, adapter : LectureRecyclerAdapter)
{
    if (view!=null && adapter!=null)
    {
        view.adapter = adapter
    }
}
