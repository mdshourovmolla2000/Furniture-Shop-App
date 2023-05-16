package com.shourov.furnitureshop.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shourov.furnitureshop.R

@Entity(tableName = "shopping_table")
data class ShoppingTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val productId: String? = "1",
    val itemImage: Int? = R.drawable.image_placeholder_square,
    val itemName: String? = "",
    val itemCompany: String? = "",
    val itemPrice: Double? = 0.0,
    val userId: Int? = 0,
    var itemQuantity: Int? = 1,
    var isSelected: Boolean? = false
)