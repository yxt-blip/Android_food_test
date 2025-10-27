package com.example.myshipingjiance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myshipingjiance.databinding.ItemFoodTipBinding
import com.example.myshipingjiance.model.FoodTip

class FoodTipAdapter(private val onItemClick: (FoodTip) -> Unit) :
    ListAdapter<FoodTip, FoodTipAdapter.FoodTipViewHolder>(FoodTipDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTipViewHolder {
        val binding = ItemFoodTipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodTipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodTipViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FoodTipViewHolder(private val binding: ItemFoodTipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }

        fun bind(item: FoodTip) {
            binding.tipImageView.setImageResource(item.iconResId)
        }
    }

    private class FoodTipDiffCallback : DiffUtil.ItemCallback<FoodTip>() {
        override fun areItemsTheSame(oldItem: FoodTip, newItem: FoodTip): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodTip, newItem: FoodTip): Boolean {
            return oldItem == newItem
        }
    }
} 