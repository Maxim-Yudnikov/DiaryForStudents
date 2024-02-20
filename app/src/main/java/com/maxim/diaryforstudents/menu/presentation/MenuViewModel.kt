package com.maxim.diaryforstudents.menu.presentation

import com.maxim.diaryforstudents.analytics.presentation.AnalyticsScreen
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Init
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.SaveAndRestore
import com.maxim.diaryforstudents.diary.presentation.DiaryScreen
import com.maxim.diaryforstudents.news.presentation.NewsScreen
import com.maxim.diaryforstudents.performance.common.domain.PerformanceInteractor
import com.maxim.diaryforstudents.performance.common.presentation.PerformanceScreen
import com.maxim.diaryforstudents.profile.presentation.ProfileScreen

class MenuViewModel(
    private val performanceInteractor: PerformanceInteractor,
    private val navigation: Navigation.Update,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Init, SaveAndRestore {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            handle { performanceInteractor.loadData() }
        }
    }

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

    fun analytics() {
        navigation.update(AnalyticsScreen)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        performanceInteractor.save(bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        performanceInteractor.restore(bundleWrapper)
    }
}