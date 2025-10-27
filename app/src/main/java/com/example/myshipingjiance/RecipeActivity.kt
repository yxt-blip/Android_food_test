package com.example.myshipingjiance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshipingjiance.adapter.FoodTipAdapter
import com.example.myshipingjiance.databinding.ActivityRecipeBinding
import com.example.myshipingjiance.model.FoodTip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private lateinit var foodTipAdapter: FoodTipAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 设置工具栏
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "食品小常识"

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // 添加编辑按钮点击事件
        findViewById<FloatingActionButton>(R.id.addTipButton).setOnClickListener {
            val intent = Intent(this, EditFoodTipActivity::class.java)
            intent.putExtra("isNewTip", true)
            startActivity(intent)
        }

        setupViews()
        setupListeners()
        loadFoodTips()
    }

    private fun setupViews() {
        // 设置RecyclerView为网格布局，每行显示3个项目
        binding.recipeRecyclerView.layoutManager = GridLayoutManager(this, 3)
        foodTipAdapter = FoodTipAdapter { foodTip ->
            openFoodTipDetail(foodTip)
        }
        binding.recipeRecyclerView.adapter = foodTipAdapter
    }

    private fun setupListeners() {
        // 搜索按钮点击
        binding.searchButton.setOnClickListener {
            // TODO: 实现食谱搜索功能
        }

        // 筛选按钮点击
        binding.filterButton.setOnClickListener {
            // TODO: 显示筛选对话框
        }
    }

    private fun loadFoodTips() {
        // 从assets文件加载食品小常识
        val tipsList = ArrayList<FoodTip>()
        
        try {
            val reader = BufferedReader(InputStreamReader(assets.open("food_tips.txt")))
            var line: String?
            var currentTitle = ""
            val currentTips = ArrayList<String>()
            
            while (reader.readLine().also { line = it } != null) {
                line?.let {
                    if (it.isNotEmpty()) {
                        if (it.matches(Regex("\\d+\\..*"))) {
                            // 如果已经有标题，先保存上一个
                            if (currentTitle.isNotEmpty() && currentTips.isNotEmpty()) {
                                addFoodTip(tipsList, currentTitle, currentTips)
                                currentTips.clear()
                            }
                            
                            // 开始新标题
                            currentTitle = it.substring(it.indexOf(".") + 1).trim()
                        } else if (it.startsWith("-")) {
                            // 是条目
                            val tipContent = it.substring(1).trim()
                            currentTips.add(tipContent)
                        }
                    }
                }
            }
            
            // 添加最后一个类别
            if (currentTitle.isNotEmpty() && currentTips.isNotEmpty()) {
                addFoodTip(tipsList, currentTitle, currentTips)
            }
            
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
            
            // 如果加载失败，添加一些默认数据
            tipsList.add(
                FoodTip(
                    1, 
                    "健康蔬菜小常识", 
                    listOf("深绿色蔬菜如菠菜和西兰花含有丰富的叶酸和维生素K，可强健骨骼，降低心脏病风险。"), 
                    R.drawable.food_vegetable
                )
            )
        }
        
        foodTipAdapter.submitList(tipsList)
    }
    
    private fun addFoodTip(
        list: ArrayList<FoodTip>, 
        title: String, 
        tips: List<String>
    ) {
        val iconResId = when {
            title.contains("蔬菜") -> R.drawable.img_vegetable
            title.contains("肉") -> R.drawable.img_meat
            title.contains("海鲜") -> R.drawable.img_seafood
            title.contains("水") -> R.drawable.img_water
            title.contains("谷物") -> R.drawable.img_guwu
            title.contains("坚果") -> R.drawable.img_nut
            else -> R.drawable.ic_food_safety
        }
        
        list.add(
            FoodTip(
                list.size + 1,
                title,
                ArrayList(tips),
                iconResId
            )
        )
    }
    
    private fun openFoodTipDetail(foodTip: FoodTip) {
        val intent = Intent(this, FoodTipDetailActivity::class.java).apply {
            putExtra(FoodTipDetailActivity.EXTRA_TIP_ID, foodTip.id)
            putExtra(FoodTipDetailActivity.EXTRA_TIP_TITLE, foodTip.title)
            putExtra(FoodTipDetailActivity.EXTRA_TIP_ICON_RES_ID, foodTip.iconResId)
            putStringArrayListExtra(FoodTipDetailActivity.EXTRA_TIP_TIPS, ArrayList(foodTip.tips))
        }
        startActivity(intent)
    }
} 