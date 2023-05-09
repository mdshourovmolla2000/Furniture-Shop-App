package com.shourov.furnitureshop.utils

import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.model.FavouriteModel
import com.shourov.furnitureshop.model.OnBoardingModel
import com.shourov.furnitureshop.model.ShoppingModel
import com.shourov.furnitureshop.model.SpecialOfferModel

class DemoData {

    fun onBoardingData(): ArrayList<OnBoardingModel> {
        val itemList: ArrayList<OnBoardingModel> = ArrayList()
        itemList.add(OnBoardingModel(R.drawable.onboarding_image_1, "View And Experience Furniture With The Help Of Augmented Reality"))
        itemList.add(OnBoardingModel(R.drawable.onboarding_image_2, "Design Your Space With Augmented Reality By Creating Room"))
        itemList.add(OnBoardingModel(R.drawable.onboarding_image_3, "Explore World Class Top Furnitures As Per Your Requirements & Choice"))
        return itemList
    }

    fun specialOfferData(): ArrayList<SpecialOfferModel> {
        val itemList: ArrayList<SpecialOfferModel> = ArrayList()
        itemList.add(SpecialOfferModel("25% Discount", "For a cozy yellow set!", "Learn More", R.drawable.special_offer_image_1))
        itemList.add(SpecialOfferModel("35% discount", "For a cozy yellow set!", "Shop Now", R.drawable.special_offer_image_2))
        itemList.add(SpecialOfferModel("25% Discount", "For a cozy yellow set!", "Learn More", R.drawable.special_offer_image_1))
        return itemList
    }

    fun favouriteData(): ArrayList<FavouriteModel> {
        val itemList: ArrayList<FavouriteModel> = ArrayList()
        itemList.add(FavouriteModel(R.drawable.favourite_item_1, "Rotating Lounge Chair", "$39.00"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_2, "Trapeziam Arm Chair", "$36.00"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_3, "Corada D3 Lounge Chair", "$45.21"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_4, "Pearl Beading Fur Textured", "$29.68"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_1, "Rotating Lounge Chair", "$39.01"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_2, "Trapeziam Arm Chair", "$36.02"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_3, "Corada D3 Lounge Chair", "$45.22"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_4, "Pearl Beading Fur Textured", "$29.69"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_1, "Rotating Lounge Chair", "$39.02"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_2, "Trapeziam Arm Chair", "$36.02"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_3, "Corada D3 Lounge Chair", "$45.23"))
        itemList.add(FavouriteModel(R.drawable.favourite_item_4, "Pearl Beading Fur Textured", "$29.67"))
        return itemList
    }

    fun shoppingData(): ArrayList<ShoppingModel> {
        val itemList: ArrayList<ShoppingModel> = ArrayList()
        itemList.add(ShoppingModel(R.drawable.shopping_item_1,"Minimalist Chair", "Regal Do Lobo", 1.00, 1, false))
        itemList.add(ShoppingModel(R.drawable.shopping_item_2,"Hallingdal Chair", "Hatil-Loren", 2.00, 1, false))
        itemList.add(ShoppingModel(R.drawable.shopping_item_3,"Hiro Armchair", "Hatil-Loren", 3.00, 1, false))
        return itemList
    }

}