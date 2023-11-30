package com.shourov.furnitureshop.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_table")
data class OrderTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val productList: String? = "",
    val itemPrice: String? = "",
    val orderStatus: String? = "Processing",
    val orderDateAndTime: String? = "",
    val userId: Int? = 0,
)