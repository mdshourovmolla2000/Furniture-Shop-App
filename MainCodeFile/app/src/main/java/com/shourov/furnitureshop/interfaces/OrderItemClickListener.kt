package com.shourov.furnitureshop.interfaces

import com.shourov.furnitureshop.database.tables.OrderTable

interface OrderItemClickListener {
    fun onOrderItemClick(currentItem: OrderTable)
}