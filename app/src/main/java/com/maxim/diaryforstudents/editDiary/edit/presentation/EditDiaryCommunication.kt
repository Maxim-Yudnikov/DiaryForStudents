package com.maxim.diaryforstudents.editDiary.edit.presentation

import com.maxim.diaryforstudents.core.presentation.Communication

interface EditDiaryCommunication : Communication.All<EditDiaryState> {
    fun setGrade(grade: Int?, userId: String, date: Int)
    class Base : Communication.RegularWithDeath<EditDiaryState>(), EditDiaryCommunication {
        override fun setGrade(grade: Int?, userId: String, date: Int) {
            liveData.value!!.setGrade(grade, userId, date)
        }
    }
}