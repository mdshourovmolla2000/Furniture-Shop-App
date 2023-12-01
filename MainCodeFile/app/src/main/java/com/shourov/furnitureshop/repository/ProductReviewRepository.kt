package com.shourov.furnitureshop.repository

import com.shourov.furnitureshop.model.ProductReviewModel
import com.shourov.furnitureshop.utils.DemoData
import java.io.IOException
import java.net.SocketException

class ProductReviewRepository() {
    fun getProductReview(productId: String?, callback: (data: List<ProductReviewModel>?, message: String?) -> Unit) {
        try {
            val response = DemoData().productReviewData().filter { it.productId == productId }

            callback(response, "Successful")
        }
        catch (e: IOException) { callback(null, "Network error") }
        catch (e: SocketException) { callback(null, "Network error") }
        catch (e: Exception) { callback(null, "Something wrong") }
    }
}