# 食品检测API接口文档

## 基本信息

- **基础URL**: `http://localhost:8080/api/v1`
- **认证方式**: JWT Bearer Token
- **数据格式**: JSON
- **字符编码**: UTF-8

## 通用响应格式

```json
{
    "code": 200,
    "message": "操作成功",
    "data": {
        
    }
}
```

### 响应状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（需要登录） |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |


## 2. 用户认证接口

### 2.1 用户注册
- **接口**: `POST /auth/register`
- **描述**: 用户注册
- **认证**: 不需要
- **请求参数**:
```json
{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "phone": "13800138000"
}
```
- **响应示例**:
```json
{
    "code": 200,
    "message": "注册成功",
    "data": {
        "userId": 1,
        "username": "testuser",
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInVzZXJJZCI6MSwiaWF0IjoxNzU0MjE5NTk4LCJleHAiOjE3NTQzMDU5OTh9.N_xAAgJcOucTTtD2IlzEGHmf0v32Yl9DXCfA-Bz7p9KozELurZjFtBEcDQ9Bq7vCL_AU5C_FdaWSCFQn_6AzCg"
    }
}
```

### 2.2 用户登录
- **接口**: `POST /auth/login`
- **描述**: 用户登录
- **认证**: 不需要
- **请求参数**:
```json
{
    "phone": "13800138000",
    "password": "123456"
}
```
- **响应示例**:
```json
{
    "code": 200,
    "message": "登录成功",
    "data": {
        "userId": 1,
        "username": "testuser",
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInVzZXJJZCI6MSwiaWF0IjoxNzU0MjE5NTk4LCJleHAiOjE3NTQzMDU5OTh9.N_xAAgJcOucTTtD2IlzEGHmf0v32Yl9DXCfA-Bz7p9KozELurZjFtBEcDQ9Bq7vCL_AU5C_FdaWSCFQn_6AzCg"
    }
}
```

## 4. 食品检测接口

### 4.1 上传图片检测
- **接口**: `POST /detection/upload`
- **描述**: 上传图片进行食品新鲜度检测
- **认证**: 需要 (Bearer Token)
- **请求头**:
```
Authorization: Bearer <token>
Content-Type: multipart/form-data
```
- **请求参数**:
  - `image`: 图片文件 (支持 jpg, jpeg, png 格式，最大 10MB)
  - `modelVersion`: 模型版本 (可选，默认 "v1.0.0")
- **响应示例**:
```json
{
    "code": 200,
    "message": "检测成功",
    "data": {
        "detectionId": 1,
        "freshnessStatus": "新鲜",
        "confidenceScore": 0.95,
        "imageUrl": "http://localhost:8080/api/v1/uploads/images/detection_1.jpg",
        "detectionTime": "2025-08-03T19:30:00"
    }
}
```

### 4.2 获取检测历史
- **接口**: `GET /detection/history`
- **描述**: 获取用户的检测历史记录
- **认证**: 需要 (Bearer Token)
- **请求头**:
```
Authorization: Bearer <token>
```
- **请求参数**:
  - `page`: 页码 (默认 1)
  - `size`: 每页大小 (默认 10)
- **响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "total": 1,
        "page": 1,
        "size": 10,
        "records": [
            {
                "detectionId": 1,
                "freshnessStatus": "新鲜",
                "confidenceScore": 0.95,
                "imageUrl": "http://localhost:8080/api/v1/uploads/images/detection_1.jpg",
                "detectionTime": "2025-08-03T19:30:00"
            }
        ]
    }
}
```

### 4.3 获取检测详情
- **接口**: `GET /detection/{id}`
- **描述**: 获取指定检测记录的详细信息
- **认证**: 需要 (Bearer Token)
- **请求头**:
```
Authorization: Bearer <token>
```
- **路径参数**:
  - `id`: 检测记录ID
- **响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "detectionId": 1,
        "freshnessStatus": "新鲜",
        "confidenceScore": 0.95,
        "imageUrl": "http://localhost:8080/api/v1/uploads/images/detection_1.jpg",
        "detectionTime": "2025-08-03T19:30:00"
    }
}
```

## 5. 统计分析接口

### 5.1 获取用户统计
- **接口**: `GET /statistics/user`
- **描述**: 获取当前用户的检测统计信息
- **认证**: 需要 (Bearer Token)
- **请求头**:
```
Authorization: Bearer <token>
```
- **请求参数**: 无
- **响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "totalDetections": 1,
        "freshCount": 1,
        "normalCount": 0,
        "spoiledCount": 0,
        "averageConfidence": 0.95,
        "weeklyTrend": [
            {
                "date": "2025-08-03",
                "count": 1
            }
        ]
    }
}
```

### 5.2 获取全局统计
- **接口**: `GET /statistics/global`
- **描述**: 获取系统全局统计信息
- **认证**: 需要 (Bearer Token)
- **请求头**:
```
Authorization: Bearer <token>
```
- **请求参数**: 无
- **响应示例**:
```json
{
    "code": 200,
    "message": "获取成功",
    "data": {
        "totalUsers": 1,
        "totalDetections": 1,
        "activeUsersToday": 1,
        "detectionsToday": 1,
        "modelAccuracy": 0.92
    }
}
```


## 7. 错误处理

### 7.1 常见错误响应

#### 认证失败 (401)
```json
{
    "code": 401,
    "message": "未授权访问",
    "data": null
}
```

#### 参数验证错误 (400)
```json
{
    "code": 400,
    "message": "用户名已存在",
    "data": null
}
```

#### 文件上传错误 (400)
```json
{
    "code": 400,
    "message": "不支持的文件格式",
    "data": null
}
```

#### 服务器错误 (500)
```json
{
    "code": 500,
    "message": "服务器内部错误",
    "data": null
}
```

## 8. Android端集成指南

### 8.1 网络请求配置

```kotlin
// 基础配置
val baseUrl = "http://localhost:8080/api/v1"
val timeout = 30L // 秒

// Retrofit配置示例
val retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .client(OkHttpClient.Builder()
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .build())
    .build()
```

### 8.2 认证Token管理

```kotlin
// Token存储
class TokenManager {
    private val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }
    
    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }
    
    fun clearToken() {
        sharedPreferences.edit().remove("token").apply()
    }
}

// 请求拦截器
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = TokenManager().getToken()
        
        return if (token != null) {
            val authenticatedRequest = request.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            chain.proceed(authenticatedRequest)
        } else {
            chain.proceed(request)
        }
    }
}
```

### 8.3 文件上传示例

```kotlin
// 图片上传
fun uploadImage(imageFile: File, token: String): Call<DetectionResponse> {
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("image", imageFile.name, 
            RequestBody.create("image/*".toMediaTypeOrNull(), imageFile))
        .addFormDataPart("modelVersion", "v1.0.0")
        .build()
    
    return apiService.uploadImage(requestBody)
}
```

### 8.4 错误处理

```kotlin
// 统一错误处理
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val message: String) : ApiResult<Nothing>()
    data class NetworkError(val exception: Exception) : ApiResult<Nothing>()
}

// 使用示例
when (val result = apiService.login(loginRequest)) {
    is ApiResult.Success -> {
        // 处理成功响应
        TokenManager().saveToken(result.data.token)
    }
    is ApiResult.Error -> {
        // 处理业务错误
        when (result.code) {
            401 -> // 处理认证失败
            400 -> // 处理参数错误
        }
    }
    is ApiResult.NetworkError -> {
        // 处理网络错误
    }
}
```

## 9. 测试用例

### 9.1 完整测试流程

3. **用户登录** → 获取认证Token
4. **上传图片检测** → 测试核心功能
5. **获取检测历史** → 验证数据存储


### 9.2 错误测试用例

1. **无效Token** → 应返回401
2. **过期Token** → 应返回401
3. **错误密码** → 应返回400
4. **重复注册** → 应返回400
5. **无效文件格式** → 应返回400

## 10. 注意事项

1. **Token有效期**: JWT Token有效期为24小时
2. **文件大小限制**: 图片文件最大10MB
3. **支持的文件格式**: jpg, jpeg, png
4. **并发限制**: 建议控制并发请求数量
5. **错误重试**: 网络错误建议实现重试机制
6. **数据缓存**: 建议对静态数据实现本地缓存

## 11. 更新日志

### v1.0.0 (2025-08-03)
- 初始版本发布
- 支持用户认证
- 支持食品检测
- 支持统计分析
- 支持模型管理 