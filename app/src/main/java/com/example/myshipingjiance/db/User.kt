package com.example.myshipingjiance.db

data class User(
    val id: Int = 0,
    val username: String = "",
    val description: String = "",
    val avatarPath: String? = null,
    val password : String ="",
    val phone : String? =""
)