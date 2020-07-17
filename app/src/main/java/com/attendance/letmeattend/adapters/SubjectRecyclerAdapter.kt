package com.attendance.letmeattend.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.LectureTtBinding
import com.attendance.letmeattend.databinding.SubjectResourceBinding
import com.attendance.letmeattend.listeners.OnLectureClickListener
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.LectureViewModel
import com.attendance.letmeattend.viewmodels.SubjectViewModel
import javax.security.auth.Subject


class SubjectRecyclerAdapter() : RecyclerView.Adapter<SubjectRecyclerAdapter.ViewHolder>() {

    private lateinit var subjects : List<SubjectModel>
    private lateinit var clickListener: OnLectureClickListener

    private var position = 0

    fun getPosition(): Int {
        return position
    }

    fun getLecture(position : Int) : SubjectModel
    {
        return subjects.get(position)
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    fun setSubjects(subjects : List<SubjectModel>)
    {
        this.subjects = subjects
        notifyDataSetChanged()

    }
//
//    fun setClickListener(callback : OnLectureClickListener)
//    {
//        this.clickListener = callback
//        notifyDataSetChanged()
//    }

    class ViewHolder(val binding: SubjectResourceBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
        val vm : SubjectViewModel = SubjectViewModel()
        private var day = 0
        fun bind(subject : SubjectModel)
        {
            binding.vm = vm
            vm.bind(subject)
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
        val binding : SubjectResourceBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.subject_resource,parent,false)
        parent.context.toast(itemCount.toString())
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       if (::subjects.isInitialized)
       {
           return subjects.size
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
        holder.bind(subjects.get(position))

    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }
}