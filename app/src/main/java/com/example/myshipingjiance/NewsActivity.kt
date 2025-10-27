package com.example.myshipingjiance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshipingjiance.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        // TODO: 初始化新闻列表视图
    }

    private fun setupListeners() {
        // TODO: 设置新闻项点击监听器
    }
} 