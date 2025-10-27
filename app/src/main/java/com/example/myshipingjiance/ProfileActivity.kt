package com.example.myshipingjiance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.myshipingjiance.databinding.ActivityProfileBinding
import com.example.myshipingjiance.db.User
import com.example.myshipingjiance.db.UserDao
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userDao: UserDao
    private var currentUser: User? = null
    
    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            // 刷新个人资料
            refreshProfileData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userDao = UserDao(this)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        // 设置工具栏
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // 加载用户资料
        loadUserProfile()
    }

    private fun setupListeners() {
        // 编辑资料按钮点击事件
        binding.editProfileButton.setOnClickListener {
            openEditProfileActivity()
        }
        
        // 头像点击事件
        binding.avatarImage.setOnClickListener {
            openEditProfileActivity()
        }
    }
    
    private fun openEditProfileActivity() {
        val intent = Intent(this, EditProfileActivity::class.java).apply {
            currentUser?.let { user ->
                putExtra("userName", user.username)
                putExtra("userDescription", user.description)
                putExtra("userAvatarPath", user.avatarPath)
            }
        }
        editProfileLauncher.launch(intent)
    }
    
    private fun loadUserProfile() {
        // 从数据库加载用户信息
        currentUser = userDao.getCurrentUser()
        
        currentUser?.let { user ->
            // 设置用户名
            binding.userNameText.text = user.username
            
            // 设置头像
            user.avatarPath?.let { path ->
                val file = File(path)
                if (file.exists()) {
                    binding.avatarImage.setImageURI(Uri.fromFile(file))
                }
            }
        }
    }
    
    private fun refreshProfileData() {
        // 刷新用户资料
        loadUserProfile()
    }
} 