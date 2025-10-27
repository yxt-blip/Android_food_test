package com.example.myshipingjiance.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.example.myshipingjiance.R
import com.example.myshipingjiance.databinding.DialogSelectImageBinding

class SelectImageDialog(
    context: Context,
    private val onCameraClick: () -> Unit,
    private val onGalleryClick: () -> Unit
) : Dialog(context, R.style.BottomDialog) {

    private lateinit var binding: DialogSelectImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSelectImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindow()
        setupClickListeners()
    }

    private fun setupWindow() {
        window?.apply {
            setGravity(Gravity.BOTTOM)
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun setupClickListeners() {
        binding.takePhotoText.setOnClickListener {
            onCameraClick()
            dismiss()
        }

        binding.chooseFromGalleryText.setOnClickListener {
            onGalleryClick()
            dismiss()
        }
    }
} 