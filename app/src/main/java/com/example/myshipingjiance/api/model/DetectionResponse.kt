package com.example.myshipingjiance.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DetectionResponse(
    @SerializedName("detectionId")
    val detectionId: Int,

    @SerializedName("freshnessStatus")
    val freshnessStatus: String,

    @SerializedName("confidenceScore")
    val confidenceScore: Double,

    @SerializedName("imageUrl")
    val imageUrl: String,

    @SerializedName("detectionTime")
    val detectionTime: String
) : Serializable
