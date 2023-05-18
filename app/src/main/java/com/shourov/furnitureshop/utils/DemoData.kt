package com.shourov.furnitureshop.utils

import com.shourov.furnitureshop.R
import com.shourov.furnitureshop.model.CategoryModel
import com.shourov.furnitureshop.model.HomeCategoryModel
import com.shourov.furnitureshop.model.OnBoardingModel
import com.shourov.furnitureshop.model.ProductImageModel
import com.shourov.furnitureshop.model.ProductModel
import com.shourov.furnitureshop.model.ShoppingModel
import com.shourov.furnitureshop.model.SpecialOfferModel

class DemoData {
    fun onBoardingData(): List<OnBoardingModel> {
        val itemList: ArrayList<OnBoardingModel> = ArrayList()
        itemList.add(OnBoardingModel(R.drawable.onboarding_image_1, "View And Experience Furniture With The Help Of Augmented Reality"))
        itemList.add(OnBoardingModel(R.drawable.onboarding_image_2, "Design Your Space With Augmented Reality By Creating Room"))
        itemList.add(OnBoardingModel(R.drawable.onboarding_image_3, "Explore World Class Top Furnitures As Per Your Requirements & Choice"))
        return itemList
    }

    fun specialOfferData(): List<SpecialOfferModel> {
        val itemList: ArrayList<SpecialOfferModel> = ArrayList()
        itemList.add(SpecialOfferModel("25% Discount", "For a cozy yellow set!", "Learn More", R.drawable.special_offer_image_1))
        itemList.add(SpecialOfferModel("35% discount", "For a cozy yellow set!", "Shop Now", R.drawable.special_offer_image_2))
        itemList.add(SpecialOfferModel("25% Discount", "For a cozy yellow set!", "Learn More", R.drawable.special_offer_image_1))
        return itemList
    }

    fun homeCategoryData(): List<HomeCategoryModel> {
        val itemList: ArrayList<HomeCategoryModel> = ArrayList()
        itemList.add(HomeCategoryModel(R.drawable.category_image_placeholder, "All"))
        itemList.add(HomeCategoryModel(R.drawable.home_category_image_1, "Armchair"))
        itemList.add(HomeCategoryModel(R.drawable.home_category_image_2, "Sofa"))
        itemList.add(HomeCategoryModel(R.drawable.home_category_image_3, "Bed"))
        itemList.add(HomeCategoryModel(R.drawable.home_category_image_4, "Light"))
        itemList.add(HomeCategoryModel(R.drawable.home_category_image_more, "More"))
        return itemList
    }

    fun productData(): List<ProductModel> {
        val itemList: ArrayList<ProductModel> = ArrayList()
        itemList.add(ProductModel("1", R.drawable.product_1_image, "Ox Mathis Chair", "Hans j. wegner", "The Ox Mathis Chair is a high-quality and comfortable chair designed for use in various settings, including offices, conference rooms, and home offices. It is made of durable materials, such as high-quality leather or fabric upholstery, and features a sturdy frame made of metal or wood. The chair is designed to provide excellent support and comfort for extended periods of sitting, with features such as adjustable height, tilt, and lumbar support. The chair's design is sleek and modern, making it a stylish addition to any workspace or home office. Overall, the Ox Mathis Chair is a reliable and comfortable seating option that is perfect for anyone looking for a high-quality chair that can withstand daily use.", "Armchair", 90.99))
        itemList.add(ProductModel("2", R.drawable.product_2_image, "Pearl Beading Fur Textured", "Hans j. wegner", "The Pearl Beading Fur Textured chair is a luxurious and stylish piece of furniture that is sure to add a touch of elegance to any room. The chair is upholstered in a soft and plush fur material that is both comfortable and inviting. The fur texture adds a cozy and warm feel to the chair, making it the perfect spot to curl up with a good book or relax after a long day.\n" +
                "\n" +
                "The chair is adorned with beautiful pearl beading that adds a touch of glamour and sophistication to the overall design. The beading is carefully placed along the edges of the chair, creating a stunning and eye-catching effect. The pearls are a beautiful contrast to the soft fur texture, creating a unique and visually appealing piece of furniture.\n" +
                "\n" +
                "The chair is designed with both style and comfort in mind. The seat and backrest are generously padded, providing a comfortable and supportive place to sit. The chair also features sturdy legs that provide stability and support, ensuring that it will last for years to come.\n" +
                "\n" +
                "Overall, the Pearl Beading Fur Textured chair is a beautiful and luxurious piece of furniture that is sure to make a statement in any room. Its unique design and attention to detail make it a standout piece that is both functional and stylish.", "Armchair", 29.68))
        itemList.add(ProductModel("3", R.drawable.product_3_image, "Hatil Sofa", "Hatil", "Hatil is a well-known furniture brand that offers a wide range of high-quality and stylish sofas. The Hatil sofa is a perfect blend of comfort and elegance, designed to enhance the overall look of your living room. The sofa is made of premium quality materials, including high-density foam, solid wood frame, and soft fabric upholstery, ensuring durability and comfort. The Hatil sofa comes in various sizes, colors, and designs, allowing you to choose the one that best suits your taste and home decor. With its sleek and modern design, the Hatil sofa is perfect for both traditional and contemporary living spaces. Whether you want to relax with your family or entertain guests, the Hatil sofa is an excellent choice for any home.", "Sofa", 350.00))
        itemList.add(ProductModel("4", R.drawable.product_4_image, "Regal Sofa", "Regal", "The Regal sofa is a luxurious and elegant piece of furniture that exudes sophistication style. It is designed with a classic and timeless silhouette that features clean lines and a sleek profile. The sofa is upholstered in high-quality, plush fabric that is soft to the touch and provides a comfortable seating experience. The backrest and armrests are generously padded, providing ample support and cushioning for the user. The sofa is supported by sturdy, tapered legs that add to its overall aesthetic appeal. The Regal sofa is perfect for any living room or lounge area, and is sure to impress guests with its refined and sophisticated design.", "Sofa", 400.00))
        itemList.add(ProductModel("5", R.drawable.product_5_image, "HATIL Bed Obsession-151", "Hatil", "A storage bed is a type of bed that is designed to provide additional storage space in the bedroom. It typically features built-in drawers or compartments that are located underneath the mattress, allowing you to store clothing, bedding, or other items out of sight. This type of bed is ideal for smaller bedrooms or for those who need extra storage space but don't want to sacrifice floor space for additional furniture. Storage beds come in a variety of styles and materials, from traditional wooden frames to modern upholstered designs, and can be found in sizes ranging from twin to king. They are a practical and stylish solution for anyone looking to maximize their bedroom storage.", "Bed", 380.00))
        return itemList
    }

    fun productImageData(): List<ProductImageModel> {
        val itemList: ArrayList<ProductImageModel> = ArrayList()
        itemList.add(ProductImageModel("1", R.drawable.product_1_image_1))
        itemList.add(ProductImageModel("1", R.drawable.product_1_image_2))
        itemList.add(ProductImageModel("1", R.drawable.product_1_image_3))
        itemList.add(ProductImageModel("1", R.drawable.product_1_image_4))
        itemList.add(ProductImageModel("2", R.drawable.product_2_image_1))
        itemList.add(ProductImageModel("2", R.drawable.product_2_image_2))
        itemList.add(ProductImageModel("2", R.drawable.product_2_image_3))
        itemList.add(ProductImageModel("2", R.drawable.product_2_image_4))
        itemList.add(ProductImageModel("3", R.drawable.product_3_image_1))
        itemList.add(ProductImageModel("3", R.drawable.product_3_image_2))
        itemList.add(ProductImageModel("3", R.drawable.product_3_image_3))
        itemList.add(ProductImageModel("3", R.drawable.product_3_image_4))
        itemList.add(ProductImageModel("4", R.drawable.product_4_image_1))
        itemList.add(ProductImageModel("4", R.drawable.product_4_image_2))
        itemList.add(ProductImageModel("4", R.drawable.product_4_image_3))
        itemList.add(ProductImageModel("4", R.drawable.product_4_image_4))
        itemList.add(ProductImageModel("5", R.drawable.product_5_image_1))
        itemList.add(ProductImageModel("5", R.drawable.product_5_image_2))
        itemList.add(ProductImageModel("5", R.drawable.product_5_image_3))
        itemList.add(ProductImageModel("5", R.drawable.product_5_image_4))
        return itemList
    }

    fun categoryData(): List<CategoryModel> {
        val itemList: ArrayList<CategoryModel> = ArrayList()
        itemList.add(CategoryModel(R.drawable.category_image_1, "Armchair"))
        itemList.add(CategoryModel(R.drawable.category_image_2, "Sofa"))
        itemList.add(CategoryModel(R.drawable.category_image_3, "Bed"))
        itemList.add(CategoryModel(R.drawable.category_image_4, "Light"))
        return itemList
    }
}