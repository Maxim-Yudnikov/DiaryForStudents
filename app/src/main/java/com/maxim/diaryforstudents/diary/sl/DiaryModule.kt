package com.maxim.diaryforstudents.diary.sl

import com.maxim.diaryforstudents.core.presentation.Formatter
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.diary.data.DayDataToDomainMapper
import com.maxim.diaryforstudents.diary.data.DiaryDataToDomainMapper
import com.maxim.diaryforstudents.diary.data.DiaryRepository
import com.maxim.diaryforstudents.diary.data.DiaryService
import com.maxim.diaryforstudents.diary.domain.DayDomainToUiMapper
import com.maxim.diaryforstudents.diary.domain.DiaryDomainToUiMapper
import com.maxim.diaryforstudents.diary.domain.DiaryInteractor
import com.maxim.diaryforstudents.diary.presentation.DiaryCommunication
import com.maxim.diaryforstudents.diary.presentation.DiaryViewModel
import com.maxim.diaryforstudents.performance.common.data.FailureHandler
import com.maxim.diaryforstudents.performance.common.data.PerformanceDataToDomainMapper
import com.maxim.diaryforstudents.performance.common.domain.PerformanceDomainToUiMapper

class DiaryModule(private val core: Core, private val clear: ClearViewModel) :
    Module<DiaryViewModel> {

    override fun viewModel() = DiaryViewModel(
        DiaryInteractor.Base(
            DiaryRepository.Base(
                core.retrofit().create(DiaryService::class.java),
                Formatter.Base,
                core.eduUser(),
                core
            ), FailureHandler.Base(), DiaryDataToDomainMapper(PerformanceDataToDomainMapper()), DayDataToDomainMapper()
        ),
        DiaryCommunication.Base(),
        core.lessonDetailsStorage(),
        core.navigation(),
        clear,
        DiaryDomainToUiMapper(PerformanceDomainToUiMapper()), DayDomainToUiMapper()
    )
}