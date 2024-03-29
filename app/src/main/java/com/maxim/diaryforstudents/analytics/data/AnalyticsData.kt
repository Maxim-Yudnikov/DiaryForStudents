package com.maxim.diaryforstudents.analytics.data

import com.maxim.diaryforstudents.analytics.domain.AnalyticsDomain

interface AnalyticsData {
    fun toDomain(): AnalyticsDomain

    data class LineCommon(
        private val data: List<Float>,
        private val labels: List<String>,
        private val quarter: Int,
        private val interval: Int
    ) : AnalyticsData {
        override fun toDomain() = AnalyticsDomain.LineCommon(data, labels, quarter, interval)
    }

    class LineMarks(
        private val fiveData: List<Float>,
        private val fourData: List<Float>,
        private val threeData: List<Float>,
        private val twoData: List<Float>,
        private val labels: List<String>
    ) : AnalyticsData {
        override fun toDomain() =
            AnalyticsDomain.LineMarks(fiveData, fourData, threeData, twoData, labels)
    }

    data class PieMarks(
        private val fiveCount: Int,
        private val fourCount: Int,
        private val threeCount: Int,
        private val twoCount: Int,
        private val oneCount: Int
    ) : AnalyticsData {
        override fun toDomain() =
            AnalyticsDomain.PieMarks(fiveCount, fourCount, threeCount, twoCount, oneCount)
    }

    data class PieFinalMarks(
        private val fiveCount: Int,
        private val fourCount: Int,
        private val threeCount: Int,
        private val twoCount: Int,
        private val oneCount: Int
    ) : AnalyticsData {
        override fun toDomain() =
            AnalyticsDomain.PieFinalMarks(fiveCount, fourCount, threeCount, twoCount, oneCount)
    }
}