package com.shourov.furnitureshop.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_table")
data class FavouriteTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val productId: String? = "1",
    val userId: Int? = 0
)