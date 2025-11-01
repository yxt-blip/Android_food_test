package com.example.myshipingjiance

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myshipingjiance.api.RetrofitClient
import com.example.myshipingjiance.api.TokenManager
import com.example.myshipingjiance.databinding.ActivityLoginBinding
import com.example.myshipingjiance.db.User
import com.example.myshipingjiance.db.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val apiService by lazy { RetrofitClient.getInstance(this) }
    private val tokenManager by lazy { TokenManager(this) }
    private val userDao by lazy { UserDao(this.applicationContext) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setPhoneAndPassword()
    }

    private fun setPhoneAndPassword() {
        lifecycleScope.launch(Dispatchers.IO) {
            val user = userDao.getCurrentUser()
            Log.d("LoginActivity", "User from DB: $user")
            withContext(Dispatchers.Main){
                user?.let {
                    binding.etPhone.setText(it.phone)
                    binding.etCode.setText(it.password)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            val code = binding.etCode.text.toString()

            if (validateInputs(phone, code)) {
                login(phone, code)
            }
        }

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(phone: String, code: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val jsonObject = JSONObject().apply {
                    put("phone", phone)
                    put("password", code) // Assuming code is the password for now
                }
                val requestBody = jsonObject.toString().toRequestBody("application/json".toMediaType())

                val response = apiService.login(requestBody)

                withContext(Dispatchers.Main) {
                    if (response.code == 200 && response.data != null) {
                        val user = User(id = 1, phone = phone, password = code)

                        // 在后台线程执行数据库操作
                        lifecycleScope.launch(Dispatchers.IO) {
                            userDao.saveOrUpdateUser(user) // <--- 调用新方法
                        }

                        tokenManager.saveToken(response.data.token)
                        Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "登录失败: ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "登录出错: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun validatePhone(phone: String): Boolean {
        if (phone.isEmpty()) {
            binding.etPhone.error = "请输入手机号"
            return false
        }
        if (!phone.matches(Regex("""^1[3-9]\d{9}$"""))) {
            binding.etPhone.error = "请输入正确的手机号"
            return false
        }
        return true
    }

    private fun validateInputs(phone: String, code: String): Boolean {
        if (!validatePhone(phone)) {
            return false
        }
        if (code.isEmpty()) {
            binding.etCode.error = "请输入密码"
            return false
        }
        //This is a simple validation, you might want to adjust it
        if (code.length < 4) {
            binding.etCode.error = "密码长度不能小于4位"
            return false
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}