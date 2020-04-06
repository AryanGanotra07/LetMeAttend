package com.attendance.letmeattend.helpers

import android.content.Intent
import com.attendance.letmeattend.models.Lecture

object LectureDeserializer {

    fun getLecture(intent : Intent?) : Lecture? {
        val lectureBundle = intent?.getBundleExtra("lecture")
        val lect = lectureBundle?.getParcelable<Lecture>("lecture")
        return lect
    }
}