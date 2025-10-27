package com.example.myshipingjiance.api.model

import com.google.gson.annotations.SerializedName

data class UserStatisticsResponse(
    @SerializedName("totalDetections")
    val totalDetections: Int,

    @SerializedName("freshCount")
    val freshCount: Int,

    @SerializedName("normalCount")
    val normalCount: Int,

    @SerializedName("spoiledCount")
    val spoiledCount: Int,

    @SerializedName("averageConfidence")
    val averageConfidence: Double,

    @SerializedName("weeklyTrend")
    val weeklyTrend: List<WeeklyTrendItem>
)

data class WeeklyTrendItem(
    @SerializedName("date")
    val date: String,

    @SerializedName("count")
    val count: Int
)
