package com.shourov.furnitureshop.repository

import androidx.lifecycle.LiveData
import com.shourov.furnitureshop.database.AppDao
import com.shourov.furnitureshop.database.tables.OrderTable

class OrderProcessingRepository(private val dao: AppDao) {
    fun getOrderData(userId: Int?): LiveData<List<OrderTable>> = dao.getOrderData(userId, "Processing")
}