package com.example.myshipingjiance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myshipingjiance.databinding.ActivityEditFoodTipBinding
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.ArrayList

class EditFoodTipActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditFoodTipBinding
    private var tipId: Int = 0
    private var tipList: ArrayList<String> = ArrayList()

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_TIPS = "extra_tips"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditFoodTipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        loadData()
        setupListeners()
    }

    private fun setupViews() {
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun loadData() {
        tipId = intent.getIntExtra(EXTRA_CATEGORY_ID, 0)
        binding.titleInput.setText(intent.getStringExtra(EXTRA_TITLE) ?: "")
        
        val tips = intent.getStringArrayListExtra(EXTRA_TIPS)
        if (!tips.isNullOrEmpty()) {
            tipList.addAll(tips)
            updateTipsDisplay()
        }
    }

    private fun setupListeners() {
        binding.addTipButton.setOnClickListener {
            val tipText = binding.tipInput.text.toString().trim()
            if (tipText.isNotEmpty()) {
                tipList.add(tipText)
                updateTipsDisplay()
                binding.tipInput.text?.clear()
            } else {
                Toast.makeText(this, "请输入小常识内容", Toast.LENGTH_SHORT).show()
            }
        }

        binding.saveButton.setOnClickListener {
            saveFoodTip()
        }
    }

    private fun updateTipsDisplay() {
        val builder = StringBuilder()
        for ((index, tip) in tipList.withIndex()) {
            builder.append("${index + 1}. $tip\n\n")
        }
        binding.tipsPreviewText.text = builder.toString()
    }

    private fun saveFoodTip() {
        val title = binding.titleInput.text.toString().trim()

        if (title.isEmpty() || tipList.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show()
            return
        }

        // 保存到应用内部存储
        try {
            val cacheDir = File(filesDir, "food_tips")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }
            
            val file = File(cacheDir, "custom_tips_$tipId.txt")
            BufferedWriter(FileWriter(file)).use { writer ->
                writer.write("$title\n")
                for (tip in tipList) {
                    writer.write("- $tip\n")
                }
            }
            
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_CATEGORY_ID, tipId)
            resultIntent.putExtra(EXTRA_TITLE, title)
            resultIntent.putStringArrayListExtra(EXTRA_TIPS, tipList)
            setResult(RESULT_OK, resultIntent)
            
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "保存失败：${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
} 