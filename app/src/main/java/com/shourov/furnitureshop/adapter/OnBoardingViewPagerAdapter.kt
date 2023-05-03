package com.shourov.furnitureshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.model.OnBoardingData
import com.shourov.furnitureshop.utils.loadImage

class OnBoardingViewPagerAdapter(private val onBoardingDataList: List<OnBoardingData>): PagerAdapter() {
    override fun getCount(): Int {
        return onBoardingDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.single_onboarding_screen_item_layout, null)

        val itemImageview: ImageView = view.findViewById(R.id.itemImageview)
        val titleTextview: TextView = view.findViewById(R.id.itemTitleTextview)

        itemImageview.loadImage(onBoardingDataList[position].imageUrl)
        titleTextview.text = onBoardingDataList[position].title

        container.addView(view)
        return view
    }
}