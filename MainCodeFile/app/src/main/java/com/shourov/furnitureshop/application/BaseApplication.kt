package com.shourov.furnitureshop.application

import android.app.Application
import androidx.room.Room
import com.shourov.furnitureshop.database.AppDatabase

class BaseApplication: Application() {

    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "com.shourov.furnitureshop"
        ).build()
    }
}