package com.shourov.furnitureshop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shourov.furnitureshop.view.profilePage.orderHistoryPage.OrderDeliveredFragment
import com.shourov.furnitureshop.view.profilePage.orderHistoryPage.OrderProcessingFragment
import com.shourov.furnitureshop.view.profilePage.orderHistoryPage.OrderShippedFragment

class OrderHistoryViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> OrderProcessingFragment()
            1-> OrderShippedFragment()
            2-> OrderDeliveredFragment()
            else-> OrderProcessingFragment()
        }
    }
}