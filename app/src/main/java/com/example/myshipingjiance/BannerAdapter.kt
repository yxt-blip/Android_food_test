package com.example.myshipingjiance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    // 轮播图片资源
    private val bannerImages = listOf(
        R.drawable.home,
        R.drawable.cake,
        R.drawable.fruit,
        R.drawable.food_vegetable
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val actualPosition = position % bannerImages.size // 关键！
        holder.imageView.setImageResource(bannerImages.get(actualPosition))
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE // 关键！
    }


    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.bannerImage)
    }
} 