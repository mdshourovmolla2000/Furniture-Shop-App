package com.shourov.furnitureshop.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address_table")
data class AddressTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var fullName: String? = "",
    var mobileNumber: String? = "",
    var fullAddress: String? = "",
    val userId: Int? = 0,
)