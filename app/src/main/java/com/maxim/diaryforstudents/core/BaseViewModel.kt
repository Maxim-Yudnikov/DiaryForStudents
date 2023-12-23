package com.maxim.diaryforstudents.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel(
    private val runAsync: RunAsync
) : ViewModel() {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    protected fun <T : Any> handle(backgroundBlock: suspend () -> T, uiBlock: (T) -> Unit) {
        runAsync.handleAsync(viewModelScope, backgroundBlock, uiBlock)
    }
}