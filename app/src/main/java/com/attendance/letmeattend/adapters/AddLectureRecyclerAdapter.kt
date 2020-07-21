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
import com.attendance.letmeattend.databinding.LectureResourceBinding
import com.attendance.letmeattend.databinding.LectureTtBinding
import com.attendance.letmeattend.databinding.SubjectResourceBinding
import com.attendance.letmeattend.listeners.LectureListeners
import com.attendance.letmeattend.listeners.OnLectureClickListener
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.models.LectureModel
import com.attendance.letmeattend.models.SubjectModel
import com.attendance.letmeattend.utils.toast
import com.attendance.letmeattend.viewmodels.LectureNewViewModel
import com.attendance.letmeattend.viewmodels.LectureViewModel
import com.attendance.letmeattend.viewmodels.SubjectViewModel
import okhttp3.internal.notify
import javax.security.auth.Subject


class AddLectureRecyclerAdapter() : RecyclerView.Adapter<AddLectureRecyclerAdapter.ViewHolder>() {

    private lateinit var lectures : ArrayList<LectureModel>
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
            lectures.add(lecture)
        }
        else {
            this.lectures = ArrayList()
            lectures.add(lecture)
        }
        notifyItemInserted(lectures.size-1)
    }

    fun setLectures(lectures : ArrayList<LectureModel>)
    {
        this.lectures = lectures
        notifyDataSetChanged()

    }

    fun getLectures() : ArrayList<LectureModel> {
        return lectures
    }

    fun setClickListener(callback : LectureListeners)
    {
        this.clickListener = callback
        notifyDataSetChanged()
    }

    fun removeLecture(position: Int) {
        this.lectures.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setLecture(position: Int, lecture: LectureModel) {
        lectures.set(position, lecture)
        notifyItemChanged(position)
    }

    class ViewHolder(val binding: LectureResourceBinding) : RecyclerView.ViewHolder(binding.root), View.OnCreateContextMenuListener {
        val vm : LectureNewViewModel = LectureNewViewModel()
        private var day = 0
        fun bind(lecture : LectureModel)
        {
            binding.vm = vm
            vm.bind(lecture)
            itemView.context

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
        val binding : LectureResourceBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.lecture_resource,parent,false)
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.itemView.setOnLongClickListener {
//            setPosition(holder.adapterPosition)
//            false
//        }
        holder.itemView.findViewById<ImageView>(R.id.delete_lecture).setOnClickListener {
            setPosition(holder.adapterPosition)
            if (clickListener!=null){
                clickListener.onLectureDelete(lectures.get(holder.adapterPosition))
            }
        }
        holder.itemView.setOnClickListener {
            setPosition(holder.adapterPosition)
            if (clickListener!=null){
                clickListener.onLectureEdit(holder.adapterPosition,lectures.get(holder.adapterPosition))
            }
        }
        holder.bind(lectures.get(position))

    }

    override fun onViewRecycled(holder: ViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }
}