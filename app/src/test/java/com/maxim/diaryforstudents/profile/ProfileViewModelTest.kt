package com.maxim.diaryforstudents.profile

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.COMMUNICATION
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.FakeRunAsync
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.fakes.REPOSITORY
import com.maxim.diaryforstudents.login.presentation.LoginScreen
import com.maxim.diaryforstudents.profile.data.GradeResult
import com.maxim.diaryforstudents.profile.data.ProfileRepository
import com.maxim.diaryforstudents.profile.presentation.ProfileCommunication
import com.maxim.diaryforstudents.profile.presentation.ProfileState
import com.maxim.diaryforstudents.profile.presentation.ProfileViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ProfileViewModelTest {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: FakeProfileRepository
    private lateinit var communication: FakeProfileCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClearViewModel
    private lateinit var runAsync: FakeRunAsync
    private lateinit var order: Order

    @Before
    fun before() {
        order = Order()
        repository = FakeProfileRepository(order)
        communication = FakeProfileCommunication(order)
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        runAsync = FakeRunAsync()
        viewModel = ProfileViewModel(repository, communication, navigation, clear, runAsync)
    }

    @Test
    fun test_init() {
        repository.mustReturn("email@gmail.com", "name", GradeResult.Student("10"))
        viewModel.init(true)
        communication.checkCalledTimes(1)
        runAsync.returnResult()
        communication.checkCalledTimes(2)
        communication.checkCalledWith(
            listOf(
                ProfileState.Loading,
                ProfileState.Base("name", GradeResult.Student("10"), "email@gmail.com")
            )
        )

        viewModel.init(false)
        communication.checkCalledTimes(2)
    }

    @Test
    fun test_sign_out() {
        viewModel.signOut()
        navigation.checkCalledWith(LoginScreen)
        clear.checkCalledWith(ProfileViewModel::class.java)
        order.check(listOf(REPOSITORY, NAVIGATION, CLEAR))
    }

    @Test
    fun test_back() {
        viewModel.back()
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(ProfileViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_save_and_restore() {
        val bundleWrapper = FakeBundleWrapper()
        viewModel.save(bundleWrapper)
        communication.checkSaveCalledTimes(1)
        communication.checkRestoreCalledTimes(0)
        communication.checkSaveCalledWith(bundleWrapper)

        viewModel.restore(bundleWrapper)
        communication.checkSaveCalledTimes(1)
        communication.checkRestoreCalledTimes(1)
        communication.checkRestoreCalledWith(bundleWrapper)

        communication.checkSaveAndRestoreWasCalledWithSameKey()
    }
}

private class FakeProfileRepository(private val order: Order) : ProfileRepository {
    private var data = Triple<String, GradeResult, String>("", GradeResult.Empty, "")
    private var counter = 0
    fun mustReturn(email: String, name: String, grade: GradeResult) {
        data = Triple(name, grade, email)
    }

    override fun signOut() {
        order.add(REPOSITORY)
        counter++
    }

    override suspend fun data(): Triple<String, GradeResult, String> = data
}

private class FakeProfileCommunication(private val order: Order) : ProfileCommunication {
    private val list = mutableListOf<ProfileState>()
    override fun update(value: ProfileState) {
        order.add(COMMUNICATION)
        list.add(value)
    }

    fun checkCalledWith(expected: List<ProfileState>) {
        assertEquals(expected, list)
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ProfileState>) {
        throw IllegalStateException("not using in test")
    }

    private val saveList = mutableListOf<BundleWrapper.Save>()
    private val restoreList = mutableListOf<BundleWrapper.Restore>()
    private var saveKey = ""
    private var restoreKey = ""
    fun checkSaveCalledTimes(expected: Int) {
        assertEquals(expected, saveList.size)
    }

    fun checkSaveCalledWith(expected: BundleWrapper.Save) {
        assertEquals(expected, saveList.last())
    }

    fun checkSaveAndRestoreWasCalledWithSameKey() {
        assertEquals(saveKey, restoreKey)
    }

    fun checkRestoreCalledTimes(expected: Int) {
        assertEquals(expected, restoreList.size)
    }

    fun checkRestoreCalledWith(expected: BundleWrapper.Restore) {
        assertEquals(expected, restoreList.last())
    }

    override fun save(key: String, bundleWrapper: BundleWrapper.Save) {
        saveList.add(bundleWrapper)
        saveKey = key
    }

    override fun restore(key: String, bundleWrapper: BundleWrapper.Restore) {
        restoreList.add(bundleWrapper)
        restoreKey = key
    }
}