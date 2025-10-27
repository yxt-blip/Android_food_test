package com.example.myshipingjiance

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myshipingjiance.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // 发送验证码按钮点击事件
        binding.sendCodeButton.setOnClickListener {
            val phoneNumber = binding.phone.text.toString()
            if (isValidPhoneNumber(phoneNumber)) {
                // TODO: 调用发送验证码的API
                startCountDownTimer()
                Toast.makeText(this, "验证码已发送", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
            }
        }

        // 注册按钮点击事件
        binding.registerButton.setOnClickListener {
            if (validateInputs()) {
                // TODO: 调用注册API
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
                // 注册成功后跳转到登录页面
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        // 登录链接点击事件
        binding.loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInputs(): Boolean {
        val phone = binding.phone.text.toString()
        val verificationCode = binding.verificationCode.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()

        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show()
            return false
        }

        if (verificationCode.length != 6) {
            Toast.makeText(this, "请输入6位验证码", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "密码长度不能少于6位", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.matches(Regex("^1[3-9]\\d{9}$"))
    }

    private fun startCountDownTimer() {
        binding.sendCodeButton.isEnabled = false
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.sendCodeButton.text = "${millisUntilFinished / 1000}秒后重试"
            }

            override fun onFinish() {
                binding.sendCodeButton.isEnabled = true
                binding.sendCodeButton.text = "获取验证码"
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
} 