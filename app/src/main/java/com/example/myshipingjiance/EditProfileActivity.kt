package com.example.myshipingjiance

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.myshipingjiance.databinding.ActivityEditProfileBinding
import com.example.myshipingjiance.db.UserDao
import com.example.myshipingjiance.dialog.SelectImageDialog
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var currentPhotoPath: String? = null
    private var photoUri: Uri? = null
    private lateinit var userDao: UserDao

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(this, "需要相机权限才能拍照", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openGallery()
        } else {
            Toast.makeText(this, "需要存储权限才能选择图片", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 处理拍照结果
            photoUri?.let { handleSelectedImage(it) }
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handleSelectedImage(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        userDao = UserDao(this)

        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        // 设置返回按钮
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // 设置当前用户信息
        binding.nameInput.setText(intent.getStringExtra("userName") ?: "")
        binding.descriptionInput.setText(intent.getStringExtra("userDescription") ?: "")
        
        // 加载头像（如果有）
        val avatarPath = intent.getStringExtra("userAvatarPath")
        if (avatarPath != null) {
            binding.avatarImage.setImageURI(Uri.parse("file://$avatarPath"))
        }
    }

    private fun setupListeners() {
        // 保存按钮点击事件
        binding.saveButton.setOnClickListener {
            val newName = binding.nameInput.text.toString().trim()
            val newDescription = binding.descriptionInput.text.toString().trim()

            if (validateInputs(newName)) {
                // 保存用户信息到数据库
                saveUserProfile(newName, newDescription)
            }
        }

        // 头像点击事件
        binding.avatarImage.setOnClickListener {
            showSelectImageDialog()
        }
        
        // 相机图标点击事件
        binding.cameraIcon.setOnClickListener {
            showSelectImageDialog()
        }
    }

    private fun showSelectImageDialog() {
        SelectImageDialog(
            this,
            onCameraClick = { checkCameraPermission() },
            onGalleryClick = { checkGalleryPermission() }
        ).show()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun checkGalleryPermission() {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            else -> {
                galleryPermissionLauncher.launch(permission)
            }
        }
    }

    private fun startCamera() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.provider",
            photoFile
        )

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }

        takePictureLauncher.launch(takePictureIntent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun handleSelectedImage(uri: Uri) {
        // 保存选中的图片URI
        photoUri = uri
        // 显示选中的图片
        binding.avatarImage.setImageURI(uri)
    }

    private fun saveUserProfile(name: String, description: String) {
        // 使用UserDao更新用户信息
        val success = userDao.updateUser(name, description, photoUri)
        
        if (success) {
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
            
            // 设置返回结果
            val resultIntent = Intent()
            resultIntent.putExtra("userName", name)
            resultIntent.putExtra("userDescription", description)
            setResult(RESULT_OK, resultIntent)
            finish()
        } else {
            Toast.makeText(this, "保存失败，请重试", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInputs(name: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
} 