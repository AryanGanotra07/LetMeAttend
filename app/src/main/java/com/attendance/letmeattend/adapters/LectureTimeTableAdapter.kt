package com.attendance.letmeattend.adapters

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.attendance.letmeattend.R
import com.attendance.letmeattend.application.AppApplication
import com.attendance.letmeattend.databinding.*
import com.attendance.letmeattend.listeners.LectureListeners
import com.attendance.letmeattend.listeners.OnLectureClickListener
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.LectureNewViewModel
import com.attendance.letmeattend.viewmodels.LectureViewModel
import com.attendance.letmeattend.viewmodels.SubjectViewModel
import javax.security.auth.Subject


class LectureTimeTableAdapter() : RecyclerView.Adapter<LectureTimeTableAdapter.ViewHolder>() {

    private lateinit var lectures : List<LectureModel>
    private lateinit var clickListener: LectureListeners
    private  val animation : Animation = AnimationUtils.loadAnimation(AppApplication?.context!!.applicationContext , R.anim.abc_slide_in_bottom)

    private var position = 0

    fun getPosition(): Int {
        return position
    }

    fun getLecture(position : Int) : LectureModel
    {
        return lectures.get(position)
    }

    fun setPosition(position: Int) {
        this.position = position
    }

    fun addLecture(lecture: LectureModel) {
        if (::lectures.isInitialized) {
            val newLectures = lectures.plus(lecture)
            this.lectures = newLectures
        }
        else {
            this.lectures = arrayOf(lecture).toList()
        }
        notifyDataSetChanged()
    }

    fun setLectures(lectures : List<LectureModel>)
    {
        this.lectures = lectures
        notifyDataSetChanged()

    }

    fun setClickListener(callback : LectureListeners)
    {
        this.clickListener = callback
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: LectureTimetableBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
        val vm : LectureNewViewModel = LectureNewViewModel()
        private var day = 0
        fun bind(lecture : LectureModel)
        {
            binding.vm = vm
            vm.bind(lecture)

//            itemView.context
//            itemView.setOnCreateContextMenuListener(this)

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
        val binding : LectureTimetableBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.lecture_timetable,parent,false)
        binding.root.startAnimation(animation)
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

    fun deleteLecture(lecture: LectureModel) {
       val mLectures =  lectures.toMutableList()
        mLectures.remove(lecture)
        setLectures(mLectures)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnLongClickListener {
            setPosition(holder.adapterPosition)
            false
        }
        holder.bind(lectures.get(position))
        holder.itemView.setOnClickListener {
            setPosition(holder.adapterPosition)
            if (clickListener != null) {
                clickListener.onLectureClick(lectures.get(holder.adapterPosition))
            }
        }
        holder.itemView.findViewById<ImageView>(R.id.edit_lecture).setOnClickListener {
                setPosition(holder.adapterPosition)
            if (clickListener!=null) {
                clickListener.onLectureEdit(holder.adapterPosition,lectures.get(holder.adapterPosition))
            }
        }
        holder.itemView.findViewById<ImageView>(R.id.delete_lecture).setOnClickListener {
                setPosition(holder.adapterPosition)
            if (clickListener!=null) {
                clickListener.onLectureDelete(lectures.get(holder.adapterPosition))
            }
        }

    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }
}