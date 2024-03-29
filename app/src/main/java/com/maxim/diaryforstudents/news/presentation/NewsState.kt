package com.maxim.diaryforstudents.news.presentation

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.io.Serializable

interface NewsState : Serializable {
    fun show(
        mainNewsLayout: View,
        mainNewsImageView: ImageView,
        mainNewsTitleTextView: TextView,
        mainNewsContentTextView: TextView,
        mainNewsTimeTextView: TextView,
        importantAdapter: NewsAdapter,
        defaultAdapter: NewsAdapter,
        listener: NewsAdapter.Listener,
    ) {}

    fun show(
        errorTextView: TextView,
        retryButton: Button,
        news: View,
        loading: View
    )

    data class Error(
        private val message: String
    ): NewsState {

        override fun show(
            errorTextView: TextView,
            retryButton: Button,
            news: View,
            loading: View
        ) {
            errorTextView.text = message
            errorTextView.visibility = View.VISIBLE
            retryButton.visibility = View.VISIBLE
            news.visibility = View.GONE
            loading.visibility = View.GONE
        }
    }

    data class Base(
        private val mainNews: NewsUi,
        private val importantNews: List<NewsUi>,
        private val defaultNews: List<NewsUi>,
    ) : NewsState {
        override fun show(
            mainNewsLayout: View,
            mainNewsImageView: ImageView,
            mainNewsTitleTextView: TextView,
            mainNewsContentTextView: TextView,
            mainNewsTimeTextView: TextView,
            importantAdapter: NewsAdapter,
            defaultAdapter: NewsAdapter,
            listener: NewsAdapter.Listener,
        ) {
            mainNews.showFitImage(mainNewsImageView)
            mainNews.showTitle(mainNewsTitleTextView)
            mainNews.showContent(mainNewsContentTextView)
            mainNews.showTime(mainNewsTimeTextView)
            mainNewsLayout.setOnClickListener {
                listener.open(mainNews)
            }
            importantAdapter.update(importantNews)
            defaultAdapter.update(defaultNews)
        }

        override fun show(
            errorTextView: TextView,
            retryButton: Button,
            news: View,
            loading: View
        ) {
            loading.visibility = View.GONE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            news.visibility = View.VISIBLE
        }
    }

    object Loading : NewsState {

        override fun show(
            errorTextView: TextView,
            retryButton: Button,
            news: View,
            loading: View
        ) {
            loading.visibility = View.VISIBLE
            errorTextView.visibility = View.GONE
            retryButton.visibility = View.GONE
            news.visibility = View.GONE
        }
    }
}