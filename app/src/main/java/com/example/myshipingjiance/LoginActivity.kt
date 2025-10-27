package com.example.myshipingjiance

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myshipingjiance.api.RetrofitClient
import com.example.myshipingjiance.api.TokenManager
import com.example.myshipingjiance.databinding.ActivityLoginBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnGetCode.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            if (validatePhone(phone)) {
                // TODO: 调用获取验证码API
                Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show()
            }
        }

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
            binding.etCode.error = "请输入验证码"
            return false
        }
        if (!code.matches(Regex("""^\d{4,6}$"""))) {
            binding.etCode.error = "请输入正确的验证码"
            return false
        }
        return true
    }
}
