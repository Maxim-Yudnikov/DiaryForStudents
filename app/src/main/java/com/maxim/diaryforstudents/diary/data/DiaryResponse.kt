package com.maxim.diaryforstudents.diary.data

import com.maxim.diaryforstudents.performance.common.data.CloudMark

data class DiaryResponse(
    val success: Boolean,
    val message: String,
    val data: List<DiaryLesson>
)

data class DiaryLesson(
    val SUBJECT_NAME: String,
    val TEACHER_NAME: String,
    val LESSON_NUMBER: Int,
    val LESSON_TIME_BEGIN: String,
    val LESSON_TIME_END: String,
    val TOPIC: String?,
    val HOMEWORK: String?,
    val HOMEWORK_PREVIOUS: DiaryPreviousHomework?,
    val MARKS: List<CloudMark>?,
    val NOTES: List<String>,
    val ABSENCE: List<DiaryAbsence>
)

data class DiaryAbsence(
    val SHORT_NAME: String
)

data class DiaryPreviousHomework(
    val DATE: String,
    val HOMEWORK: String
)