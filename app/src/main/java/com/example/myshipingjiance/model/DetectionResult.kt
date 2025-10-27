package com.example.myshipingjiance.model

import java.util.Date

data class DetectionResult(
    val id: Int,
    val result: String,    // 检测结果
    val confidence: Float, // 置信度
    val isSafe: Boolean,   // 是否安全
    val timestamp: Date,   // 检测时间
    val notes: String = "" // 备注
)
