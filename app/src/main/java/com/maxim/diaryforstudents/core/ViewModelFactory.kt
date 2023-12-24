package com.maxim.diaryforstudents.core

import androidx.lifecycle.ViewModel
import com.maxim.diaryforstudents.login.presentation.LoginCommunication
import com.maxim.diaryforstudents.login.presentation.LoginViewModel
import com.maxim.diaryforstudents.login.data.LoginCloudDataSource
import com.maxim.diaryforstudents.login.data.LoginRepository
import com.maxim.diaryforstudents.main.MainViewModel
import com.maxim.diaryforstudents.profile.ProfileViewModel

interface ViewModelFactory : ProvideViewModel, ClearViewModel {
    class Base(
        private val provider: ProvideViewModel
    ) : ViewModelFactory {
        private val map = mutableMapOf<Class<out ViewModel>, ViewModel>()
        override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
            if (map[clazz] == null)
                map[clazz] = provider.viewModel(clazz)
            return map[clazz] as T
        }

        override fun clearViewModel(clazz: Class<out ViewModel>) {
            map.remove(clazz)
        }
    }
}

interface ProvideViewModel {
    fun <T : ViewModel> viewModel(clazz: Class<T>): T

    class Base(private val core: Core, private val clear: ClearViewModel) : ProvideViewModel {
        override fun <T : ViewModel> viewModel(clazz: Class<T>): T {
            return when (clazz) {
                MainViewModel::class.java -> MainViewModel(core.navigation())
                LoginViewModel::class.java -> LoginViewModel(
                    LoginRepository.Base(LoginCloudDataSource.Base(core.dataBase())),
                    LoginCommunication.Base(),
                    core.navigation(),
                    clear,
                    ManageResource.Base(core.resourceManager())
                )
                ProfileViewModel::class.java -> ProfileViewModel()
                else -> throw IllegalStateException("unknown viewModel $clazz")
            } as T
        }
    }
}

interface ClearViewModel {
    fun clearViewModel(clazz: Class<out ViewModel>)
}