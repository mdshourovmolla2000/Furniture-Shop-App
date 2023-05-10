package com.shourov.furnitureshop.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.shourov.furnitureshop.database.tables.UserTable

@Dao
interface AppDao {

    @Query("SELECT COUNT(*) FROM user_table WHERE LOWER(email) = LOWER(:email)")
    suspend fun checkIfUserExists(email: String?): Int

    @Insert
    suspend fun insertUser(user: UserTable?): Long

    @Query("SELECT * FROM user_table WHERE LOWER(email) = LOWER(:email) AND password = :password")
    suspend fun checkIfUserIsValid(email: String?, password: String?): UserTable?

}