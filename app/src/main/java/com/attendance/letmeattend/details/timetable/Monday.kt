package com.attendance.letmeattend.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModelProviders
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.MondayBinding

import com.attendance.letmeattend.details.listeners.AddSubjectListener
import com.attendance.letmeattend.details.listeners.OnLectureClickListener
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel



class Monday(): Fragment() {

    private lateinit var add_btn : ImageButton
    private lateinit var add_sub_callback : AddSubjectListener
    private lateinit var viewModel : EnterDetailsViewModel
    private lateinit var binding : MondayBinding
    private lateinit var lectures : MediatorLiveData<ArrayList<Lecture>>
    private var position : Int = 0


    fun newInstance(page: Int): Monday? {
        val day : Monday = Monday()
        day.position = page
        return day
    }




    fun setAddSubjectListener (callback: AddSubjectListener)
    {
        add_sub_callback = callback
    }
    private lateinit var onLectureClickListener: OnLectureClickListener

    fun setOnLectureClickListener(callback : OnLectureClickListener)
    {
        onLectureClickListener = callback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.monday, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_btn = view.findViewById(R.id.add_btn)
        add_btn.setOnClickListener { add_sub_callback.onAddSubject(0) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity!=null) {
            viewModel = ViewModelProviders.of(activity!!).get(EnterDetailsViewModel::class.java)
            binding.vm = viewModel
            binding.id = 0

            Log.i("FragmentID",position.toString())
            binding.lifecycleOwner = this
            viewModel.getMonLectureRecyclerAdapter().setClickListener(onLectureClickListener)
//            binding.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
//            binding.recyclerView.adapter = viewModel.getLectureRecyclerAdapter()
        }
//
//        fun setPosition(pos : Int)
//        {
//            position = pos
//        }

//        lectures = viewModel?.getLectures()
//        lectures.observe(this, Observer {
//            if (it!=null) {
//                context?.toast("Lectures obtained in fragment")
//               // viewModel.getLectureRecyclerAdapter().setLectures(it)
//            }
//            else context?.toast("No lecture found")
//        })


    }
}