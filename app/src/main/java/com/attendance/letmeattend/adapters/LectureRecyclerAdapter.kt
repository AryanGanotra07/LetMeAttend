package com.attendance.letmeattend.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.LectureTtBinding
import com.attendance.letmeattend.details.listeners.OnLectureClickListener
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.LectureViewModel

class LectureRecyclerAdapter() : RecyclerView.Adapter<LectureRecyclerAdapter.ViewHolder>() {

    private lateinit var lectures : ArrayList<Lecture>
    private lateinit var clickListener: OnLectureClickListener

    fun setLectures(lectures : ArrayList<Lecture>)
    {
        this.lectures = lectures
        notifyDataSetChanged()

    }

    fun setClickListener(callback : OnLectureClickListener)
    {
        this.clickListener = callback
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: LectureTtBinding, val clickListener: OnLectureClickListener) : RecyclerView.ViewHolder(binding.root) {
        val vm : LectureViewModel = LectureViewModel()
        fun bind(lecture : Lecture)
        {
            binding.vm = vm
            vm.bind(lecture)
            if (clickListener != null)
            {
                itemView.setOnClickListener {
                    clickListener.onLectureClick(lecture)
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : LectureTtBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.lecture_tt,parent,false)
        parent.context.toast(itemCount.toString())
        return ViewHolder(binding,clickListener)
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