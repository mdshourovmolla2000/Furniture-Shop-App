package com.shourov.furnitureshop.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shourov.furnitureshop.R

@Entity(tableName = "cart_table")
data class CartTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val productId: String? = "1",
    val itemImage: Int? = R.drawable.loading_image,
    val itemName: String? = "",
    val itemCompany: String? = "",
    val itemPrice: Double? = 0.0,
    val userId: Int? = 0,
    var itemQuantity: Int? = 1,
    var isSelected: Boolean? = false
)