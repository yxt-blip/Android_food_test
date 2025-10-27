package com.example.myshipingjiance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myshipingjiance.R
import com.example.myshipingjiance.databinding.ItemDetectionResultBinding
import com.example.myshipingjiance.model.DetectionResult
import java.text.SimpleDateFormat
import java.util.Locale

class DetectionResultAdapter(private val onItemClick: (DetectionResult) -> Unit) :
    ListAdapter<DetectionResult, DetectionResultAdapter.ResultViewHolder>(ResultDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val binding = ItemDetectionResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ResultViewHolder(private val binding: ItemDetectionResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(item: DetectionResult) {
            binding.resultText.text = item.result
            binding.confidenceText.text = "置信度: ${(item.confidence * 100).toInt()}%"

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            binding.timestampText.text = dateFormat.format(item.timestamp)

            if (item.isSafe) {
                binding.safetyIndicator.text = "安全"
                binding.safetyIndicator.setBackgroundResource(R.drawable.bg_safe)
            } else {
                binding.safetyIndicator.text = "不安全"
                binding.safetyIndicator.setBackgroundResource(R.drawable.bg_unsafe)
            }

            // Set a default image since imageUri is no longer part of the DetectionResult
            binding.resultImage.setImageResource(R.drawable.ic_food_safety)
        }
    }

    private class ResultDiffCallback : DiffUtil.ItemCallback<DetectionResult>() {
        override fun areItemsTheSame(oldItem: DetectionResult, newItem: DetectionResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DetectionResult, newItem: DetectionResult): Boolean {
            return oldItem == newItem
        }
    }
}
