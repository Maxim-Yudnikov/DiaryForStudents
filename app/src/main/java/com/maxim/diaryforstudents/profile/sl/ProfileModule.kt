package com.maxim.diaryforstudents.profile.sl

import com.maxim.diaryforstudents.core.sl.ClearViewModel
import com.maxim.diaryforstudents.core.sl.Core
import com.maxim.diaryforstudents.core.sl.Module
import com.maxim.diaryforstudents.profile.data.ProfileCloudDataSource
import com.maxim.diaryforstudents.profile.data.ProfileRepository
import com.maxim.diaryforstudents.profile.presentation.ProfileCommunication
import com.maxim.diaryforstudents.profile.presentation.ProfileViewModel

class ProfileModule(private val core: Core, private val clear: ClearViewModel) :
    Module<ProfileViewModel> {
    override fun viewModel() = ProfileViewModel(
        ProfileRepository.Base(
            ProfileCloudDataSource.Base(
                core.service(), core.clientWrapper(),
                core.lessonsMapper(),
                core.myUser()
            ),
            core.myUser()
        ),
        ProfileCommunication.Base(),
        core.navigation(),
        clear,
    )
}