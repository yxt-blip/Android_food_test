package com.example.myshipingjiance.api.model

/**
 * 通用API响应体
 * @param T 具体的业务数据类型
 */
data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
)
