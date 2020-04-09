package com.attendance.letmeattend.fragments.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.FridayBinding
import com.attendance.letmeattend.listeners.AddSubjectListener
import com.attendance.letmeattend.listeners.CustomOnBoomListener
import com.attendance.letmeattend.listeners.OnLectureClickListener
import com.attendance.letmeattend.models.Lecture
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.nightonke.boommenu.Animation.BoomEnum
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton
import kotlinx.android.synthetic.main.monday.*
import java.util.*
import kotlin.collections.ArrayList

class Friday():Fragment() {

    private lateinit var add_btn : ImageButton
    private lateinit var add_sub_callback : AddSubjectListener
    private lateinit var viewModel : EnterDetailsViewModel
    private lateinit var binding : FridayBinding
    private lateinit var lectures : MediatorLiveData<ArrayList<Lecture>>
    private var position : Int = 0
    private val day = Calendar.FRIDAY

    fun newInstance(page: Int): Friday? {
        val day : Friday = Friday()
        day.position = page
        return day
    }
    private lateinit var onLectureClickListener: OnLectureClickListener

    fun setOnLectureClickListener(callback : OnLectureClickListener)
    {
        onLectureClickListener = callback
    }


    fun setAddSubjectListener (callback: AddSubjectListener)
    {
        add_sub_callback = callback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater , R.layout.friday, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // add_btn = view.findViewById(R.id.add_btn)
       // add_btn.setOnClickListener { add_sub_callback.onAddSubject(4) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity!=null) {
            viewModel = ViewModelProviders.of(activity!!).get(EnterDetailsViewModel::class.java)
           binding.vm = viewModel
            binding.id = day
            viewModel.getFriLectureRecyclerAdapter().setClickListener(onLectureClickListener)

           // Log.i("FragmentID",position.toString())
            binding.lifecycleOwner = this
//            binding.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL,false)
//            binding.recyclerView.adapter = viewModel.getLectureRecyclerAdapter()
        }
        bmb.isInFragment =true
        bmb.clearBuilders()

        val builder = TextInsideCircleButton.Builder()
            .normalText("ADD NEW")
            .normalColor(context!!.getColor(R.color.colorAccent))
        bmb.addBuilder(builder)

        for (i in 1 until bmb.getPiecePlaceEnum().pieceNumber()) {
            val builder = TextInsideCircleButton.Builder()
                .normalTextRes(R.string.edit_text_error)
                .normalColor(context!!.getColor(R.color.colorAccent))
                .unable(true)
                .unableColor(context!!.getColor(R.color.windowBackground))
            bmb.addBuilder(builder)
            if (bmb.getBoomButton(i)!=null) {
                //bmb.getBoomButton(i).setBackgroundColor(context!!.getColor(R.color.colorAccent))
            }
        }


        viewModel.getSubjects().observe(this, Observer {
                value ->
            bmb.clearBuilders()

            val builder = TextInsideCircleButton.Builder()
                .normalImageDrawable(context!!.getDrawable(R.drawable.ic_create_black_24dp))
                .normalColor(context!!.getColor(R.color.colorAccent))
            bmb.addBuilder(builder)
            for (i in 0 until bmb.getPiecePlaceEnum().pieceNumber()-1) {
                if (value!=null && value.size>i) {
                    val builder = TextInsideCircleButton.Builder()
                        .normalText(value.get(i).name)
                        .normalImageDrawable(context!!.getDrawable(R.drawable.ic_add_black_24dp))
                        .normalColor(value.get(i).color)
                    bmb.addBuilder(builder)
                } else {
                    val builder = TextInsideCircleButton.Builder()
                        .normalColor(context!!.getColor(R.color.windowBackground))
                        .unable(true)
                        .unableColor(context!!.getColor(R.color.windowBackground))
                    bmb.addBuilder(builder)
                }

            }

            bmb.boomEnum = BoomEnum.PARABOLA_4


        })

        bmb.setOnBoomListener(CustomOnBoomListener(day,add_sub_callback,viewModel))
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