package com.attendance.letmeattend.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.LectureTtBinding
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.LectureViewModel

class LectureRecyclerAdapter() : RecyclerView.Adapter<LectureRecyclerAdapter.ViewHolder>() {

    private lateinit var lectures : ArrayList<Lecture>

    fun setLectures(lectures : ArrayList<Lecture>)
    {
        this.lectures = lectures
        notifyDataSetChanged()

    }

    class ViewHolder(val binding: LectureTtBinding) : RecyclerView.ViewHolder(binding.root) {
        val vm : LectureViewModel = LectureViewModel()
        fun bind(lecture : Lecture)
        {
            binding.vm = vm
            vm.bind(lecture)


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : LectureTtBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.lecture_tt,parent,false)
        parent.context.toast(itemCount.toString())
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       if (::lectures.isInitialized)
       {
           return lectures.size
       }
        else{
           return 0
       }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(lectures.get(position))
    }
}