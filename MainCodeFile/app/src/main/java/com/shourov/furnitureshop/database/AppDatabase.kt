package com.shourov.furnitureshop.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.database.tables.UserTable

@Database(entities = [UserTable::class, FavouriteTable::class, ShoppingTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao() : AppDao
}