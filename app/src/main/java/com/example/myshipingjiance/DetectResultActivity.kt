package com.example.myshipingjiance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myshipingjiance.databinding.ActivityDetectResultBinding
import com.example.myshipingjiance.ui.freshness.FreshnessResultFragment

class DetectResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectResultBinding
    private var freshnessPercentage = 20 // 默认新鲜度为20%，表示腐败状态
    private var foodName = "有机草莓"
    private var purchaseDate = "2023-06-15"
    private var expiryDate = "2023-06-19"
    private var storageLocation = "冰箱-冷藏室"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 获取Intent中的数据，如果有的话
        intent.extras?.let { bundle ->
            freshnessPercentage = bundle.getInt("freshness_percentage", freshnessPercentage)
            foodName = bundle.getString("food_name", foodName)
            purchaseDate = bundle.getString("purchase_date", purchaseDate)
            expiryDate = bundle.getString("expiry_date", expiryDate)
            storageLocation = bundle.getString("storage_location", storageLocation)
        }

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        // 设置标题栏
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "检测结果"
        }
        
        // 设置检测结果文本
        binding.resultText.text = "食品新鲜度：$freshnessPercentage%"
        
        // 根据新鲜度设置描述文本
        if (freshnessPercentage >= 80) {
            binding.resultDescription.text = "该食品处于新鲜状态，适合食用。"
        } else if (freshnessPercentage >= 50) {
            binding.resultDescription.text = "该食品状态较新鲜，建议近期食用。"
        } else if (freshnessPercentage >= 30) {
            binding.resultDescription.text = "该食品即将过期，建议尽快食用。"
        } else {
            binding.resultDescription.text = "该食品已腐败，不建议食用。"
        }
    }

    private fun setupListeners() {
        // 返回按钮点击
        binding.backButton.setOnClickListener {
            finish()
        }

        // 重新检测按钮点击
        binding.retryButton.setOnClickListener {
            // 返回上一页，准备重新拍照检测
            finish()
        }

        // 查看详情按钮点击
        binding.detailsButton.setOnClickListener {
            // 显示FreshnessResultFragment
            showFreshnessDetailFragment()
        }
    }
    
    private fun showFreshnessDetailFragment() {
        // 创建FreshnessResultFragment实例
        val fragment = FreshnessResultFragment.newInstance(
            freshnessPercentage,
            foodName,
            purchaseDate, 
            expiryDate, 
            storageLocation
        )
        
        // 将Fragment添加到容器中
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right, 
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }
} 