package com.maxim.diaryforstudents.news

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maxim.diaryforstudents.core.presentation.BundleWrapper
import com.maxim.diaryforstudents.core.presentation.Screen
import com.maxim.diaryforstudents.fakes.CLEAR
import com.maxim.diaryforstudents.fakes.FakeBundleWrapper
import com.maxim.diaryforstudents.fakes.FakeClearViewModel
import com.maxim.diaryforstudents.fakes.FakeNavigation
import com.maxim.diaryforstudents.fakes.NAVIGATION
import com.maxim.diaryforstudents.fakes.OPEN_NEWS_DATA
import com.maxim.diaryforstudents.fakes.Order
import com.maxim.diaryforstudents.news.data.NewsData
import com.maxim.diaryforstudents.news.data.NewsDataToUiMapper
import com.maxim.diaryforstudents.news.presentation.NewsCommunication
import com.maxim.diaryforstudents.news.presentation.NewsState
import com.maxim.diaryforstudents.news.presentation.NewsUi
import com.maxim.diaryforstudents.news.presentation.NewsViewModel
import com.maxim.diaryforstudents.openNews.OpenNewsScreen
import com.maxim.diaryforstudents.openNews.OpenNewsStorage
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class NewsViewModelTest {
    private lateinit var viewModel: NewsViewModel
    private lateinit var repository: FakeNewsRepository
    private lateinit var communication: FakeNewsCommunication
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClearViewModel
    private lateinit var order: Order
    private lateinit var openNewsData: FakeOpenNewsData

    @Before
    fun before() {
        order = Order()
        repository = FakeNewsRepository()
        communication = FakeNewsCommunication()
        navigation = FakeNavigation(order)
        clear = FakeClearViewModel(order)
        openNewsData = FakeOpenNewsData(order)
        viewModel = NewsViewModel(
            repository, communication, navigation,
            clear, openNewsData, NewsDataToUiMapper()
        )
    }

    @Test
    fun test_init() {
        viewModel.init(true)
        repository.checkCalledTimes(1)
        repository.checkCalledWith(viewModel)
        communication.checkCalledTimes(1)
        communication.checkCalledWith(NewsState.Loading)

        viewModel.init(false)
        repository.checkCalledTimes(2)
        communication.checkCalledTimes(1)
    }

    @Test
    fun test_reload() {
        repository.expected(
                NewsData.Base("Main news", "Content", 55, "url1"),
                listOf(NewsData.Base("Title1", "Content1", 56, "url2")),
                listOf(NewsData.Base("Title2", "Content1", 57, "url3")),
        )
        viewModel.reload()
        communication.checkCalledTimes(1)
        communication.checkCalledWith(
            NewsState.Base(
                NewsUi.Base("Main news", "Content", 55, "url1"),
                listOf(
                    NewsUi.Base("Title1", "Content1", 56, "url2")
                ),
                listOf(
                    NewsUi.Base("Title2", "Content1", 57, "url3")
                ),
            )
        )
    }

    @Test
    fun test_error() {
        viewModel.error("message")
        communication.checkCalledTimes(1)
        communication.checkCalledWith(NewsState.Error("message"))
    }

    @Test
    fun test_back() {
        viewModel.goBack()
        repository.checkCheckNewsCalledTimes(1)
        navigation.checkCalledWith(Screen.Pop)
        clear.checkCalledWith(NewsViewModel::class.java)
        order.check(listOf(NAVIGATION, CLEAR))
    }

    @Test
    fun test_open() {
        val news = NewsUi.Base("Title", "Content", 55, "")
        viewModel.open(news)
        openNewsData.checkCalledWith(news)
        navigation.checkCalledWith(OpenNewsScreen)
        order.check(listOf(OPEN_NEWS_DATA, NAVIGATION))
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

private class FakeOpenNewsData(private val order: Order) : OpenNewsStorage.Save {
    private lateinit var value: NewsUi
    fun checkCalledWith(expected: NewsUi) {
        assertEquals(expected, value)
    }

    override fun save(value: NewsUi) {
        this.value = value
        order.add(OPEN_NEWS_DATA)
    }
}

private class FakeNewsCommunication : NewsCommunication {
    private val list = mutableListOf<NewsState>()
    override fun update(value: NewsState) {
        list.add(value)
    }

    fun checkCalledWith(expected: NewsState) {
        assertEquals(expected, list.last())
    }

    fun checkCalledTimes(expected: Int) {
        assertEquals(expected, list.size)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<NewsState>) {
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