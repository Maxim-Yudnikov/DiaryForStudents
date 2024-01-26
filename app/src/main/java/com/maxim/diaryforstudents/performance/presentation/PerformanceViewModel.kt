package com.maxim.diaryforstudents.performance.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.RunAsync
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.performance.data.PerformanceData
import com.maxim.diaryforstudents.performance.eduData.EduPerformanceRepository

class PerformanceViewModel(
    private val repository: EduPerformanceRepository,
    private val communication: PerformanceCommunication,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    runAsync: RunAsync = RunAsync.Base()
) : BaseViewModel(runAsync), Communication.Observe<PerformanceState> {
    private var type: MarksType = MarksType.Base
    private var search = ""

    fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(PerformanceState.Loading)
            handle({ repository.init() }) {
                val list = repository.cachedData()
                communication.update(PerformanceState.Base(3, list.map { it.toUi() }, false))
            }
        }
    }

    fun changeType(type: MarksType) {
        this.type = type
        search(search)
    }

    fun search(search: String) {
        this.search = search
        val list = type.search(repository, search)
        communication.update(PerformanceState.Base(3, list.map { it.toUi() }, type.isFinal()))
    }

    fun changeQuarter(new: Int) {
//        repository.changeQuarter(new)
//        if (type != ACTUAL)
//            changeType(ACTUAL)
//        communication.update(
//            PerformanceState.Base(
//                new,
//                repository.data(search).map { it.toUi() },
//                true
//            )
//        )
    }

    fun back() {
        navigation.update(Screen.Pop)
        clear.clearViewModel(PerformanceViewModel::class.java)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<PerformanceState>) {
        communication.observe(owner, observer)
    }
}

interface MarksType {
    fun search(repository: EduPerformanceRepository, search: String): List<PerformanceData>
    fun isFinal(): Boolean

    object Base : MarksType {
        override fun search(
            repository: EduPerformanceRepository,
            search: String
        ) = repository.cachedData(search)

        override fun isFinal() = false
    }

    object Final : MarksType {
        override fun search(
            repository: EduPerformanceRepository,
            search: String
        ) = repository.cachedFinalData(search)

        override fun isFinal() = true
    }
}