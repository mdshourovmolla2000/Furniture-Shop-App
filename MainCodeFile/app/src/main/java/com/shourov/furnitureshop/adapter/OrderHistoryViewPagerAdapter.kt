package com.shourov.furnitureshop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shourov.furnitureshop.view.profilePage.orderHistoryPage.DeliveredFragment
import com.shourov.furnitureshop.view.profilePage.orderHistoryPage.ProcessingFragment
import com.shourov.furnitureshop.view.profilePage.orderHistoryPage.ShippedFragment

class OrderHistoryViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0-> ProcessingFragment()
            1-> ShippedFragment()
            2-> DeliveredFragment()
            else-> ProcessingFragment()
        }
    }
}