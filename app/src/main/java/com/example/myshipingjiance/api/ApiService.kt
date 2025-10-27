package com.example.myshipingjiance.api

import com.example.myshipingjiance.api.model.ApiResponse
import com.example.myshipingjiance.api.model.AuthResponse
import com.example.myshipingjiance.api.model.DetectionHistoryResponse
import com.example.myshipingjiance.api.model.DetectionResponse
import com.example.myshipingjiance.api.model.UserStatisticsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // 2.1 用户注册
    @POST("auth/register")
    suspend fun register(@Body requestBody: RequestBody): ApiResponse<AuthResponse>

    // 2.2 用户登录
    @POST("auth/login")
    suspend fun login(@Body requestBody: RequestBody): ApiResponse<AuthResponse>

    // 4.1 上传图片检测
    @Multipart
    @POST("detection/analyze") // Updated endpoint
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("modelVersion") modelVersion: RequestBody? = null
    ): ApiResponse<DetectionResponse>

    // 4.2 获取检测历史
    @GET("detection/history")
    suspend fun getDetectionHistory(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10
    ): ApiResponse<DetectionHistoryResponse>

    // 4.3 获取检测详情
    @GET("detection/{id}")
    suspend fun getDetectionDetail(@Path("id") id: Int): ApiResponse<DetectionResponse>

    // 5.1 获取用户统计
    @GET("statistics/user")
    suspend fun getUserStatistics(): ApiResponse<UserStatisticsResponse>
}
