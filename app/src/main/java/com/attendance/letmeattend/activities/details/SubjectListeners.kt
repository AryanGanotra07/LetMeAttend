package com.attendance.letmeattend.activities.details

import com.attendance.letmeattend.models.SubjectModel

interface SubjectListeners {

    fun onSubjectEdit(subjectModel: SubjectModel)
    fun onSubjectDelete(subjectModel: SubjectModel)
    fun onSubjectClicked(subjectModel: SubjectModel)
}