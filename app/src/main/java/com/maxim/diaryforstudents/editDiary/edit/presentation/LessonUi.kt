package com.maxim.diaryforstudents.editDiary.edit.presentation

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar

interface LessonUi {
    fun showNames(adapter: StudentNamesAdapter) {}
    fun showLessonsAndGrades(adapter: EditGradesAdapter) {}
    data class Students(private val students: List<StudentUi>): LessonUi {
        override fun showNames(adapter: StudentNamesAdapter) {
            adapter.update(students)
        }
    }

    data class Lesson(private val lessons: List<GradeUi>): LessonUi {
        override fun showLessonsAndGrades(adapter: EditGradesAdapter) {
            adapter.update(lessons)
        }
    }
}

interface GradeUi {
    fun show(textView: TextView)

    fun setGrade(listener: EditGradesAdapter.Listener, grade: Int?)

    class Base(private val date: Int, private val userId: String, private val grade: Int?): GradeUi {
        override fun show(textView: TextView) {
            grade?.let { textView.text = it.toString() }
        }

        override fun setGrade(listener: EditGradesAdapter.Listener, grade: Int?) {
            listener.setGrade(grade, userId, date)
        }
    }

    class Date(private val date: Int): GradeUi {
        override fun show(textView: TextView) {
            val formatter = SimpleDateFormat("dd.MM") //todo formatter
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = date * 86400000L
            textView.text = formatter.format(calendar.time)
        }

        override fun setGrade(listener: EditGradesAdapter.Listener, grade: Int?) {
            TODO("Not yet implemented")
        }
    }
}