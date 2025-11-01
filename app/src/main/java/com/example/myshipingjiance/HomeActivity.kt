package com.example.myshipingjiance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.example.myshipingjiance.adapter.MainViewPagerAdapter
import com.example.myshipingjiance.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. 设置 ViewPager2 的适配器
        binding.mainViewPager.adapter = MainViewPagerAdapter(this)

        // 2. 设置滑动页面时，底部导航栏也跟着切换
        binding.mainViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNav.menu[position].isChecked = true
            }
        })

        // 3. 设置点击底部导航栏时，ViewPager2也切换到对应页面
        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    binding.mainViewPager.currentItem = 1
                    true
                }
                R.id.navigation_history -> {
                    binding.mainViewPager.currentItem = 0
                    true
                }
                R.id.navigation_profile -> {
                    binding.mainViewPager.currentItem = 2
                    true
                }
                else -> false
            }
        }

        // 4. 设置 Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "首页"
    }
}