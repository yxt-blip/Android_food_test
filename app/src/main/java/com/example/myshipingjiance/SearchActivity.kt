package com.example.myshipingjiance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshipingjiance.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        // TODO: 初始化搜索视图
    }

    private fun setupListeners() {
        // TODO: 设置搜索相关监听器
    }
} 