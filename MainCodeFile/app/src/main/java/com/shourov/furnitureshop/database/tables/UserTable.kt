package com.shourov.furnitureshop.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserTable(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String? = "",
    val email: String? = "",
    var password: String? = "",
    var profilePic: String? = ""
)