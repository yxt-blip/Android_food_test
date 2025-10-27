package com.example.myshipingjiance

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.myshipingjiance.api.model.DetectionResponse
import com.example.myshipingjiance.databinding.FreshnessResultLayoutBinding
import kotlin.math.roundToInt

class FreshnessResultActivity : AppCompatActivity() {

    private lateinit var binding: FreshnessResultLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FreshnessResultLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }

        val result = intent.getSerializableExtra("detectionResult") as? DetectionResponse
        val photoUriString = intent.getStringExtra("photo_uri")

        if (result == null || photoUriString == null) {
            Toast.makeText(this, "无法获取检测结果或图片", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        updateUi(result, Uri.parse(photoUriString))
    }

    private fun updateUi(result: DetectionResponse, photoUri: Uri) {
        val confidencePercent = (result.confidenceScore * 100).roundToInt()

        binding.tvStatus.text = "${result.freshnessStatus}\n${confidencePercent}%"

        // Use Glide to reliably load the original image from the local URI
        Glide.with(this)
            .load(photoUri)
            .into(binding.ivFoodSafety)

        when (result.freshnessStatus.lowercase()) {
            "fresh", "新鲜" -> {
                binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.color_fresh))
                setIndicatorPosition(0.9f)
            }
            "normal", "一般", "即将过期" -> {
                binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.color_normal))
                setIndicatorPosition(0.5f)
            }
            "spoiled", "不新鲜", "腐败" -> {
                binding.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.color_spoiled))
                setIndicatorPosition(0.1f)
            }
            else -> {
                binding.tvStatus.setTextColor(Color.GRAY)
                setIndicatorPosition(0.5f)
            }
        }
    }

    private fun setIndicatorPosition(percentage: Float) {
        binding.viewColorBar.post {
            val barWidth = binding.viewColorBar.width
            val indicatorWidth = binding.ivIndicator.width
            val newX = (barWidth * percentage) - (indicatorWidth / 2f)
            binding.ivIndicator.x = newX.coerceIn(0f, (barWidth - indicatorWidth).toFloat())
        }
    }
}
