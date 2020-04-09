package com.attendance.letmeattend.details

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.attendance.letmeattend.R
import com.attendance.letmeattend.databinding.MondayBinding
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


class Monday(): Fragment() {

   // private lateinit var add_btn : ImageButton
    private lateinit var add_sub_callback : AddSubjectListener
    private lateinit var viewModel : EnterDetailsViewModel
    private lateinit var binding : MondayBinding
    private lateinit var lectures : MediatorLiveData<ArrayList<Lecture>>
    private var position : Int = 0
    private var day = Calendar.MONDAY
    private final val TAG = "Monday"


    fun newInstance(page: Int): Monday? {
        val day : Monday = Monday()
        day.position = page
        return day
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        day = arguments!!.getInt("index")
//        Log.d(TAG, position.toString())

    }

    override fun onResume() {
        super.onResume()

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  add_btn = view.findViewById(R.id.add_btn)
       // add_btn.setOnClickListener { add_sub_callback.onAddSubject(0) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity!=null) {
            viewModel = ViewModelProviders.of(activity!!).get(EnterDetailsViewModel::class.java)
            binding.vm = viewModel
            binding.id = day

            Log.i("FragmentID",position.toString())
            binding.lifecycleOwner = this
            viewModel.getMonLectureRecyclerAdapter().setClickListener(onLectureClickListener)
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

    }

}