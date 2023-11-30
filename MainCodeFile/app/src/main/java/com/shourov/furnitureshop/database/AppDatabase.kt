package com.shourov.furnitureshop.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shourov.furnitureshop.database.tables.AddressTable
import com.shourov.furnitureshop.database.tables.CartTable
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.database.tables.OrderTable
import com.shourov.furnitureshop.database.tables.UserTable

@Database(entities = [UserTable::class, FavouriteTable::class, CartTable::class, AddressTable::class, OrderTable::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao() : AppDao
}