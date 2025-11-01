package com.example.myshipingjiance.ui.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.setMargins
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.myshipingjiance.BannerAdapter
import com.example.myshipingjiance.RecipeActivity
import com.example.myshipingjiance.NewsActivity
import com.example.myshipingjiance.ResultActivity
import com.example.myshipingjiance.FreshnessResultActivity
import com.example.myshipingjiance.DetectingActivity
import com.example.myshipingjiance.R
import com.example.myshipingjiance.databinding.FragmentHomeBinding
import java.io.File
import java.util.logging.Handler

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var photoUri: Uri? = null
    private lateinit var bannerViewPager: ViewPager2
    private lateinit var indicatorContainer: LinearLayout
    private lateinit var bannerAdapter: BannerAdapter

    // 自动轮播的Handler和Runnable
    private val autoScrollHandler = android.os.Handler(Looper.getMainLooper())
    private lateinit var autoScrollRunnable: Runnable

    // 相机启动器
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            photoUri?.let { uri ->
                activity?.let {
                    val intent = Intent(it, DetectingActivity::class.java)
                    intent.putExtra("photo_uri", uri.toString())
                    it.startActivity(intent)
                }
            }
        }
    }

    // 权限请求
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "需要相机权限才能进行食品检测", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 初始化视图
        bannerViewPager = view.findViewById(R.id.bannerViewPager)
        indicatorContainer = view.findViewById(R.id.indicatorContainer)

        // 2. 设置适配器
        setupBanner()

        // 3. 设置指示器
        setupIndicators()

        // 4. 设置页面切换监听，用于同步指示器状态
        setupPageChangeListener()

        // 5. 设置自动轮播
        setupAutoScroll()
        setupListeners()
    }
    private fun setupBanner() {
        bannerAdapter = BannerAdapter()
        bannerViewPager.adapter = bannerAdapter

        // 为了实现无限循环，将起始位置设置在一个很大的数，但能被图片数量整除
        // 这样用户既可以向右滑，也可以向左滑
        val initialPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2 % 4)
        bannerViewPager.setCurrentItem(initialPosition, false)
    }

    private fun setupIndicators() {
        indicatorContainer.removeAllViews() // 清除XML中预设的静态圆点
        val indicators = Array(4) { ImageView(requireContext()) }
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }

        for (i in indicators.indices) {
            indicators[i].setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.dot_normal) // 默认状态
            )
            indicators[i].layoutParams = layoutParams
            indicatorContainer.addView(indicators[i])
        }
    }
    private fun setupPageChangeListener() {
        bannerViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position % 4) // 使用取模计算当前实际位置
            }
        })
    }

    private fun updateIndicators(currentPosition: Int) {
        for (i in 0 until indicatorContainer.childCount) {
            val indicator = indicatorContainer.getChildAt(i) as ImageView
            if (i == currentPosition) {
                indicator.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.dot_selected) // 选中状态
                )
            } else {
                indicator.setImageDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.dot_normal) // 未选中状态
                )
            }
        }
    }

    private fun setupAutoScroll() {
        autoScrollRunnable = object : Runnable {
            override fun run() {
                bannerViewPager.currentItem = bannerViewPager.currentItem + 1
                autoScrollHandler.postDelayed(this, 3000)
            }
        }
    }

    // 在Fragment可见时开始自动轮播
    override fun onResume() {
        super.onResume()
        autoScrollHandler.postDelayed(autoScrollRunnable, 1000) // 3秒后开始轮播
    }

    // 在Fragment不可见时停止自动轮播，防止内存泄漏和不必要的消耗
    override fun onPause() {
        super.onPause()
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
    }



    private fun setupListeners() {
        // 食品检测按钮点击
        binding.detectButton.setOnClickListener {
            requestPermission.launch(Manifest.permission.CAMERA)
        }

        // 食谱按钮点击
        binding.recipeButton.setOnClickListener {
            startActivity(Intent(requireContext(), RecipeActivity::class.java))
        }

        // 资讯按钮点击
        binding.newsButton.setOnClickListener {
            startActivity(Intent(requireContext(), NewsActivity::class.java))
        }

        // 健康贴士按钮点击
        binding.healthTipsButton.setOnClickListener {
            Toast.makeText(requireContext(), "健康贴士功能开发中...", Toast.LENGTH_SHORT).show()
        }

        // 健康书籍按钮点击
        binding.booksButton.setOnClickListener {
            Toast.makeText(requireContext(), "健康书籍功能开发中...", Toast.LENGTH_SHORT).show()
        }

        // 关于我们按钮点击
        binding.aboutButton.setOnClickListener {
            Toast.makeText(requireContext(), "关于我们功能开发中...", Toast.LENGTH_SHORT).show()
        }

        // 使用说明按钮点击
        binding.helpButton.setOnClickListener {
            Toast.makeText(requireContext(), "使用说明功能开发中...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val photoFile = File(requireContext().externalCacheDir, "photo.jpg")
        photoUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().applicationContext.packageName}.provider",
            photoFile
        )
        takePicture.launch(photoUri)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}