package com.example.myshipingjiance.api.model

import com.google.gson.annotations.SerializedName

data class DetectionHistoryResponse(
    @SerializedName("total")
    val total: Int,

    @SerializedName("page")
    val page: Int,

    @SerializedName("size")
    val size: Int,

    @SerializedName("records")
    val records: List<DetectionResponse>
)
