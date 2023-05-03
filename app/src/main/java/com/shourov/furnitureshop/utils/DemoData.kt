package com.shourov.furnitureshop.utils

import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.model.OnBoardingData

class DemoData {

    fun onBoardingData(): ArrayList<OnBoardingData> {
        val itemList: ArrayList<OnBoardingData> = ArrayList()
        itemList.add(OnBoardingData(R.drawable.onboarding_image_1, "View And Exprience Furniture With The Help Of Augmented Reality"))
        itemList.add(OnBoardingData(R.drawable.onboarding_image_2, "Design Your Space With Augmented Reality By Creating Room"))
        itemList.add(OnBoardingData(R.drawable.onboarding_image_3, "Explore World Class Top Furnitures As Per Your Requirements & Choice"))
        return itemList
    }

}