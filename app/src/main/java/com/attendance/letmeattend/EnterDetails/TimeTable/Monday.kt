package com.attendance.letmeattend.EnterDetails

import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.attendance.letmeattend.EnterDetails.Listeners.AddSubjectListener
import com.attendance.letmeattend.Model.Lecture
import com.attendance.letmeattend.R
import com.attendance.letmeattend.Utils.toast
import com.attendance.letmeattend.ViewModels.EnterDetailsViewModel

class Monday(): Fragment() {

    private lateinit var add_btn : ImageButton
    private lateinit var add_sub_callback : AddSubjectListener
    private lateinit var viewModel : EnterDetailsViewModel
    private lateinit var lectures : MediatorLiveData<ArrayList<Lecture>>


    fun setAddSubjectListener (callback: AddSubjectListener)
    {
        add_sub_callback = callback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.time_table_syntax,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_btn = view.findViewById(R.id.add_btn)
        add_btn.setOnClickListener { add_sub_callback.onAddSubject(id) }



    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity!=null) {
            viewModel = ViewModelProviders.of(activity!!).get(EnterDetailsViewModel::class.java)
        }

        lectures = viewModel?.getLectures()
        lectures.observe(this, Observer {
            if (it!=null) {
                context?.toast("Lectures obtained in fragment")
            }
            else context?.toast("No lecture found")
        })


    }
}