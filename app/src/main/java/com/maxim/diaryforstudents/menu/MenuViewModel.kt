package com.maxim.diaryforstudents.menu

import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen

class MenuViewModel(
    private val navigation: Navigation.Update
) : BaseViewModel() {
    fun diary() {
        navigation.update(DiaryScreen)
    }

    fun performance() {
        navigation.update(PerformanceScreen)
    }

    fun profile() {
        navigation.update(ProfileScreen)
    }

    fun news() {
        navigation.update(NewsScreen)
    }
}