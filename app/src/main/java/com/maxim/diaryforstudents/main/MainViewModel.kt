package com.maxim.diaryforstudents.main

import androidx.lifecycle.LifecycleOwner
import com.maxim.diaryforstudents.core.presentation.BaseViewModel
import com.maxim.diaryforstudents.core.presentation.Communication
import com.maxim.diaryforstudents.core.presentation.Navigation
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.login.presentation.LoginScreen

class MainViewModel(
    private val navigation: Navigation.Mutable,
) : BaseViewModel(), Communication.Observe<Screen> {
    fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            navigation.update(LoginScreen)
    }

    override fun observe(owner: LifecycleOwner, observer: androidx.lifecycle.Observer<Screen>) {
        navigation.observe(owner, observer)
    }
}