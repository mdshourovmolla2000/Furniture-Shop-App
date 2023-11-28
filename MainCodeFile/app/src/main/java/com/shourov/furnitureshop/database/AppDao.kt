package com.shourov.furnitureshop.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.shourov.furnitureshop.database.tables.AddressTable
import com.shourov.furnitureshop.database.tables.FavouriteTable
import com.shourov.furnitureshop.database.tables.ShoppingTable
import com.shourov.furnitureshop.database.tables.UserTable

@Dao
interface AppDao {
    @Query("SELECT COUNT(*) FROM user_table WHERE LOWER(email) = LOWER(:email)")
    suspend fun checkIfUserExists(email: String?): Int

    @Insert
    suspend fun insertUser(user: UserTable): Long

    @Update
    suspend fun updateUser(user: UserTable): Int

    @Query("SELECT * FROM user_table WHERE LOWER(email) = LOWER(:email) AND password = :password")
    suspend fun checkIfUserIsValid(email: String?, password: String?): UserTable?

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getUserInfo(id: Int?): LiveData<UserTable?>

    @Insert
    suspend fun insertFavourite(favourite: FavouriteTable)

    @Query("SELECT * FROM favourite_table WHERE userId = :userId")
    fun getFavouriteData(userId: Int?): LiveData<List<FavouriteTable>>

    @Delete
    suspend fun deleteFavourite(favourite: FavouriteTable)

    @Query("DELETE FROM favourite_table WHERE userId = :userId AND productId = :productId")
    suspend fun deleteFavouriteById(userId: Int?, productId: String?)

    @Query("SELECT COUNT(*) FROM favourite_table WHERE userId = :userId AND productId = :productId")
    fun checkIfProductIsInFavourite(userId: Int?, productId: String?): LiveData<Int>

    @Query("SELECT * FROM shopping_table WHERE userId = :userId")
    fun getShoppingData(userId: Int?): LiveData<List<ShoppingTable>>

    @Insert
    suspend fun insertShopping(shopping: ShoppingTable)

    @Query("SELECT COUNT(*) FROM shopping_table WHERE userId = :userId AND productId = :productId")
    fun checkIfProductIsInShopping(userId: Int?, productId: String?): LiveData<Int>

    @Query("DELETE FROM shopping_table WHERE userId = :userId AND productId = :productId")
    suspend fun deleteShoppingById(userId: Int?, productId: String?)

    @Update
    suspend fun updateShopping(shopping: ShoppingTable)

    @Delete
    suspend fun deleteShopping(shoppingList: List<ShoppingTable>)

    @Query("UPDATE shopping_table SET isSelected = :isSelected")
    suspend fun clearShoppingSelection(isSelected: Boolean? = false)

    @Query("SELECT COUNT(*) FROM shopping_table WHERE userId = :userId")
    suspend fun shoppingItemCount(userId: Int?): Int

    @Insert
    suspend fun insertAddress(address: AddressTable): Long

    @Delete
    suspend fun deleteAddress(address: AddressTable)

    @Update
    suspend fun updateAddress(address: AddressTable): Int

    @Query("SELECT * FROM address_table WHERE userId = :userId")
    fun getAddressData(userId: Int?): LiveData<List<AddressTable>>
}