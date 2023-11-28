package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.model.CategoryModel
import com.shourov.furnitureshop.utils.DemoData
import java.io.IOException
import java.net.SocketException

class CategoryRepository() {
    fun getCategory(callback: (data: List<CategoryModel>?, message: String?) -> Unit) {
        try {
            val response = DemoData().categoryData()

            callback(response, "Successful")
        }
        catch (e: IOException) { callback(null, "Network error") }
        catch (e: SocketException) { callback(null, "Network error") }
        catch (e: Exception) { callback(null, "Something wrong") }
    }

    fun categoryProductCount(categoryName: String?): Int = DemoData().productData().count { it.itemCategory == categoryName }
}