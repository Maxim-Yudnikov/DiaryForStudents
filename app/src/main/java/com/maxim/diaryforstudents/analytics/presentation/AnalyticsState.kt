package com.maxim.diaryforstudents.analytics.presentation

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import java.io.Serializable

interface AnalyticsState: Serializable {
    fun show(
        adapter: AnalyticsAdapter,
        progressBar: ProgressBar,
        errorTextView: TextView,
        retryButton: Button
    )

    object Loading : AnalyticsState {
        override fun show(
            adapter: AnalyticsAdapter,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button
        ) {
            adapter.update(emptyList())
            progressBar.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }

    data class Error(private val message: String) : AnalyticsState {
        override fun show(
            adapter: AnalyticsAdapter,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button
        ) {
            adapter.update(listOf(AnalyticsUi.Error))
            progressBar.visibility = View.GONE
            errorTextView.text = message
            errorTextView.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE
        }
    }

    data class Base(
        private val data: List<AnalyticsUi>
    ) : AnalyticsState {
        override fun show(
            adapter: AnalyticsAdapter,
            progressBar: ProgressBar,
            errorTextView: TextView,
            retryButton: Button
        ) {
            adapter.update(data)
            progressBar.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
        }
    }
}