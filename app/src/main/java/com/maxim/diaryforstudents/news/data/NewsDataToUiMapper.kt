package com.maxim.diaryforstudents.news.data

import com.maxim.diaryforstudents.news.presentation.NewsUi

class NewsDataToUiMapper : NewsData.Mapper<NewsUi> {
    override fun map(
        title: String,
        content: String,
        date: Long,
        photoUrl: String,
        downloadUrl: String,
        fileName: String
    ): NewsUi = NewsUi.Base(title, content, date, photoUrl, downloadUrl, fileName)

    override fun map() = NewsUi.Empty

    override fun map(message: String) = NewsUi.Failure(message)
}