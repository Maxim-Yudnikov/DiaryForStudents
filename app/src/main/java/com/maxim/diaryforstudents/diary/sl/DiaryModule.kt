package com.maxim.diaryforstudents.diary.sl

import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.data.DayDataToDomainMapper
import com.maxim.diaryforstudents.diary.data.DiaryDataToDomainMapper
import com.maxim.diaryforstudents.diary.domain.DiaryInteractor
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.data.DiaryService
import com.maxim.diaryforstudents.diary.domain.DayDomainToUiMapper
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryUi
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.performance.data.FailureHandler
import com.maxim.diaryforstudents.performance.data.PerformanceDataToDomainMapper
import com.maxim.diaryforstudents.performance.domain.PerformanceDomainToUiMapper
import com.maxim.diaryforstudents.performance.presentation.PerformanceUi

class DiaryModule(private val core: Core, private val clear: ClearViewModel) :
    Module<DiaryViewModel> {

    private val homeworkFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Mark>
        ) = homework.isNotEmpty()
    }
    private val topicFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Mark>
        ) = topic.isNotEmpty()
    }
    private val marksFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Mark>
        ) = marks.isNotEmpty()
    }
    private val nameFilter = object : DiaryUi.Mapper<Boolean> {
        override fun map(
            name: String, topic: String, homework: String,
            startTime: String, endTime: String, date: Int, marks: List<PerformanceUi.Mark>
        ) = true
    }

    override fun viewModel() = DiaryViewModel(
        mutableListOf(homeworkFilter, topicFilter, marksFilter, nameFilter),
        DiaryInteractor.Base(
            DiaryRepository.Base(
                core.retrofit().create(DiaryService::class.java),
                Formatter.Base,
                core.eduUser(),
                core.simpleStorage()
            ), FailureHandler.Base(), DiaryDataToDomainMapper(PerformanceDataToDomainMapper()), DayDataToDomainMapper()
        ),
        DiaryCommunication.Base(),
        core.lessonDetailsStorage(),
        core.navigation(),
        clear,
        DiaryDomainToUiMapper(PerformanceDomainToUiMapper()), DayDomainToUiMapper()
    )
}