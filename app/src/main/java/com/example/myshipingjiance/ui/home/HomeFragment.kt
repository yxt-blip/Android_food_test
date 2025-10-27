package com.example.myshipingjiance.ui.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.myshipingjiance.BannerAdapter
import com.example.myshipingjiance.RecipeActivity
import com.example.myshipingjiance.NewsActivity
import com.example.myshipingjiance.ResultActivity
import com.example.myshipingjiance.FreshnessResultActivity
import com.example.myshipingjiance.DetectingActivity
import com.example.myshipingjiance.databinding.FragmentHomeBinding
import java.io.File

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var photoUri: Uri? = null

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
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        // 设置轮播图
        binding.bannerViewPager.adapter = BannerAdapter()
        // 设置初始位置到中间，这样可以向左滑动
        binding.bannerViewPager.setCurrentItem(Int.MAX_VALUE / 2, false)
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