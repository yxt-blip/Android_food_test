package com.example.myshipingjiance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshipingjiance.adapter.DetectionResultAdapter
import com.example.myshipingjiance.databinding.ActivityResultBinding
import com.example.myshipingjiance.model.DetectionResult
import java.util.Date

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var resultAdapter: DetectionResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        loadResults()
    }

    private fun setupViews() {
        // 设置标题栏
        binding.toolbar.setNavigationOnClickListener { finish() }

        // 设置RecyclerView
        binding.resultsRecyclerView.layoutManager = LinearLayoutManager(this)
        resultAdapter = DetectionResultAdapter { result ->
            openResultDetail(result)
        }
        binding.resultsRecyclerView.adapter = resultAdapter
    }

    private fun loadResults() {
        // TODO: 实际应用中，这里应该从数据库加载检测结果
        val results = getDummyResults()
        if (results.isEmpty()) {
            binding.emptyView.visibility = android.view.View.VISIBLE
        } else {
            binding.emptyView.visibility = android.view.View.GONE
            resultAdapter.submitList(results)
        }
    }

    private fun openResultDetail(result: DetectionResult) {
        // TODO: 实现检测结果详情页面
        // val intent = Intent(this, ResultDetailActivity::class.java)
        // intent.putExtra("RESULT_ID", result.id)
        // startActivity(intent)
    }

    // 生成测试数据
    private fun getDummyResults(): List<DetectionResult> {
        return listOf(
            DetectionResult(
                id = 1,
                result = "健康食品",
                confidence = 0.95f,
                isSafe = true,
                timestamp = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000) // 昨天
            ),
            DetectionResult(
                id = 2,
                result = "可疑食品",
                confidence = 0.75f,
                isSafe = false,
                timestamp = Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000), // 前天
                notes = "建议不要食用"
            ),
            DetectionResult(
                id = 3,
                result = "新鲜蔬菜",
                confidence = 0.98f,
                isSafe = true,
                timestamp = Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000) // 3天前
            )
        )
    }
} 