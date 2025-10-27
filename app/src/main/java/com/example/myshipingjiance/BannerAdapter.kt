package com.example.myshipingjiance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    // 轮播图片资源
    private val bannerImages = listOf(
        R.drawable.home
     
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val actualPosition = position % bannerImages.size
        holder.imageView.setImageResource(bannerImages[actualPosition])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.bannerImage)
    }
} 