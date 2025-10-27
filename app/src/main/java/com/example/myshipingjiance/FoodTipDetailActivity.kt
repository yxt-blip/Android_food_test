package com.example.myshipingjiance

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myshipingjiance.databinding.ActivityFoodTipDetailBinding

class FoodTipDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFoodTipDetailBinding
    private var tipId: Int = 0
    private var category: String = ""
    private var title: String = ""
    private var iconResId: Int = 0
    private var tips: ArrayList<String> = arrayListOf()

    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                // 更新小常识内容
                title = data.getStringExtra(EditFoodTipActivity.EXTRA_TITLE) ?: title
                val updatedTips = data.getStringArrayListExtra(EditFoodTipActivity.EXTRA_TIPS)
                if (updatedTips != null) {
                    tips.clear()
                    tips.addAll(updatedTips)
                }
                
                // 刷新界面显示
                refreshDisplay()
            }
        }
    }

    companion object {
        const val EXTRA_TIP_ID = "extra_tip_id"
        const val EXTRA_TIP_TITLE = "extra_tip_title"
        const val EXTRA_TIP_ICON_RES_ID = "extra_tip_icon_res_id"
        const val EXTRA_TIP_TIPS = "extra_tip_tips"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodTipDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()
        loadTipDetails()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_food_tip_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                openEditActivity()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViews() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadTipDetails() {
        tipId = intent.getIntExtra(EXTRA_TIP_ID, 0)
        title = intent.getStringExtra(EXTRA_TIP_TITLE) ?: ""
        iconResId = intent.getIntExtra(EXTRA_TIP_ICON_RES_ID, 0)
        val receivedTips = intent.getStringArrayListExtra(EXTRA_TIP_TIPS)
        if (receivedTips != null) {
            tips.addAll(receivedTips)
        }
        
        refreshDisplay()
    }
    
    private fun refreshDisplay() {
        // 设置标题
        binding.collapsingToolbar.title = title
        binding.tipTitleText.text = title
        
        // 设置头部图片
        binding.tipHeaderImage.setImageResource(iconResId)

        // 清除旧的小常识条目
        binding.tipsContainer.removeAllViews()
        
        // 添加每条小常识
        for (tip in tips) {
            val tipView = LayoutInflater.from(this).inflate(
                R.layout.item_tip_point, 
                binding.tipsContainer, 
                false
            )
            
            val tipText = tipView.findViewById<TextView>(R.id.tipPointText)
            tipText.text = tip
            
            binding.tipsContainer.addView(tipView)
        }
    }
    
    private fun openEditActivity() {
        val intent = Intent(this, EditFoodTipActivity::class.java).apply {
            putExtra(EditFoodTipActivity.EXTRA_CATEGORY_ID, tipId)
            putExtra(EditFoodTipActivity.EXTRA_TITLE, title)
            putStringArrayListExtra(EditFoodTipActivity.EXTRA_TIPS, tips)
        }
        editLauncher.launch(intent)
    }
} 