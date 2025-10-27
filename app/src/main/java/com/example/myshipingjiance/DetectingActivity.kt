package com.example.myshipingjiance

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myshipingjiance.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class DetectingActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var currentDot = 0
    private lateinit var dots: Array<ImageView>
    private val dotCount = 4

    private val apiService by lazy { RetrofitClient.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detecting)

        setupLoadingAnimation()

        val photoUriString = intent.getStringExtra("photo_uri")
        if (photoUriString == null) {
            Toast.makeText(this, "未获取到图片", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        uploadAndDetect(photoUriString)
    }

    private fun uploadAndDetect(photoUriString: String) {
        val photoUri = Uri.parse(photoUriString)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val imagePart = uriToMultipartBodyPart(photoUri)
                if (imagePart == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetectingActivity, "图片处理失败", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    return@launch
                }

                val response = apiService.uploadImage(imagePart)

                withContext(Dispatchers.Main) {
                    if (response.code == 200 && response.data != null) {
                        val intent = Intent(this@DetectingActivity, FreshnessResultActivity::class.java).apply {
                            putExtra("detectionResult", response.data)
                            putExtra("photo_uri", photoUriString) // Pass original URI
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@DetectingActivity, "检测失败: ${response.message}", Toast.LENGTH_LONG).show()
                    }
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetectingActivity, "网络请求出错: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                    finish()
                }
            }
        }
    }

    private fun uriToMultipartBodyPart(uri: Uri): MultipartBody.Part? {
        return try {
            val fileName = getFileName(uri) ?: "image.jpg"
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val requestBody = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", fileName, requestBody)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            contentResolver.query(uri, null, null, null, null)?.use {
                if (it.moveToFirst()) {
                    val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        result = it.getString(displayNameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result
    }

    private fun setupLoadingAnimation() {
        val dotsLayout = findViewById<LinearLayout>(R.id.dotsLayout)
        dots = Array(dotCount) {
            ImageView(this).apply {
                setImageResource(R.drawable.dot_normal)
                val params = LinearLayout.LayoutParams(24, 24).apply { setMargins(8, 0, 8, 0) }
                layoutParams = params
                dotsLayout.addView(this)
            }
        }
        animateDots()
    }

    private fun animateDots() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                dots.forEachIndexed { index, imageView ->
                    imageView.setImageResource(if (index == currentDot) R.drawable.dot_selected else R.drawable.dot_normal)
                }
                currentDot = (currentDot + 1) % dotCount
                handler.postDelayed(this, 400)
            }
        }, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
