package com.maxim.diaryforstudents.login.data

import com.maxim.diaryforstudents.core.ClearViewModel
import com.maxim.diaryforstudents.core.Navigation
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.LoginState
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.profile.ProfileScreen

interface LoginResult {
    fun map(communication: LoginCommunication.Update, navigation: Navigation.Update, clear: ClearViewModel)
    data class Failed(private val message: String) : LoginResult {
        override fun map(
            communication: LoginCommunication.Update,
            navigation: Navigation.Update,
            clear: ClearViewModel
        ) {
            communication.update(LoginState.Failed(message))
        }
    }

    object Successful : LoginResult {
        override fun map(
            communication: LoginCommunication.Update,
            navigation: Navigation.Update,
            clear: ClearViewModel
        ) {
            navigation.update(ProfileScreen)
            clear.clearViewModel(LoginViewModel::class.java)
        }
    }
}