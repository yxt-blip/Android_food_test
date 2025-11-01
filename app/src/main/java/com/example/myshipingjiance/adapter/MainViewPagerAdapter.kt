package com.example.myshipingjiance.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myshipingjiance.ui.history.HistoryFragment
import com.example.myshipingjiance.ui.home.HomeFragment
import com.example.myshipingjiance.ui.profile.ProfileFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryFragment()
            1 -> HomeFragment()
            2 -> ProfileFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}