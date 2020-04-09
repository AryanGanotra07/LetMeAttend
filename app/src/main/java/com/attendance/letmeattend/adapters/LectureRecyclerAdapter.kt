package com.attendance.letmeattend.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.LectureTtBinding
import com.attendance.letmeattend.listeners.OnLectureClickListener
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.LectureViewModel


class LectureRecyclerAdapter() : RecyclerView.Adapter<LectureRecyclerAdapter.ViewHolder>() {

    private lateinit var lectures : ArrayList<Lecture>
    private lateinit var clickListener: OnLectureClickListener

    private var position = 0

    fun getPosition(): Int {
        return position
    }

    fun getLecture(position : Int) : Lecture
    {
        return lectures.get(position)
    }

    fun setPosition(position: Int) {
        this.position = position
    }

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

    class ViewHolder(val binding: LectureTtBinding, val clickListener: OnLectureClickListener) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
        val vm : LectureViewModel = LectureViewModel()
        private var day = 0
        fun bind(lecture : Lecture)
        {
            binding.vm = vm
            day = lecture.day
            vm.bind(lecture)
            if (clickListener != null)
            {
                itemView.setOnClickListener {
                    clickListener.onLectureClick(lecture)
                }
            }
            itemView.context
            itemView.setOnCreateContextMenuListener(this)

        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.setHeaderTitle("Select The Action");
            menu?.add(day,R.id.edit_lecture, 0, "Edit");//groupId, itemId, order, title
            menu?.add(day, R.id.delete_lecture, 0, "Delete");

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
        holder.itemView.setOnLongClickListener {
            setPosition(holder.adapterPosition)
            false
        }
        holder.bind(lectures.get(position))

    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }
}