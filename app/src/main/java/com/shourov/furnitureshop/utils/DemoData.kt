package com.shourov.furnitureshop.utils

import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.model.OnBoardingData
import com.shourov.furnitureshop.model.SpecialOfferData

class DemoData {

    fun onBoardingData(): ArrayList<OnBoardingData> {
        val itemList: ArrayList<OnBoardingData> = ArrayList()
        itemList.add(OnBoardingData(R.drawable.onboarding_image_1, "View And Experience Furniture With The Help Of Augmented Reality"))
        itemList.add(OnBoardingData(R.drawable.onboarding_image_2, "Design Your Space With Augmented Reality By Creating Room"))
        itemList.add(OnBoardingData(R.drawable.onboarding_image_3, "Explore World Class Top Furnitures As Per Your Requirements & Choice"))
        return itemList
    }

    fun specialOfferData(): ArrayList<SpecialOfferData> {
        val itemList: ArrayList<SpecialOfferData> = ArrayList()
        itemList.add(SpecialOfferData("25% Discount", "For a cozy yellow set!", "Learn More", R.drawable.special_offer_image_1))
        itemList.add(SpecialOfferData("35% discount", "For a cozy yellow set!", "Shop Now", R.drawable.special_offer_image_2))
        itemList.add(SpecialOfferData("25% Discount", "For a cozy yellow set!", "Learn More", R.drawable.special_offer_image_1))
        return itemList
    }

}