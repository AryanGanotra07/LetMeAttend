package com.attendance.letmeattend.listeners

import com.attendance.letmeattend.models.Subject
import com.attendance.letmeattend.viewmodels.EnterDetailsViewModel
import com.nightonke.boommenu.BoomButtons.BoomButton
import com.nightonke.boommenu.OnBoomListener

class CustomOnBoomListener (val day : Int,
                            val add_sub_callback : AddSubjectListener,
                            val viewModel : EnterDetailsViewModel ) : OnBoomListener {
    init {

    }
    override fun onBoomDidShow() {

    }

    override fun onBackgroundClick() {

    }

    override fun onClicked(index: Int, boomButton: BoomButton?) {
        if (index == 0)
        {
            add_sub_callback.onAddSubject(day)
        }
        else
        {
            val subject : Subject = viewModel.getSubjects().value!!.get(index-1)
            add_sub_callback.onAddSubject(day,subject)
        }

    }

    override fun onBoomDidHide() {

    }

    override fun onBoomWillHide() {

    }

    override fun onBoomWillShow() {

    }
}