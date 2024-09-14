package com.emarket.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface CartDAO {

    @Query("SELECT * FROM items WHERE totalOrder > 0")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemById(itemId: String): ItemEntity

    @Update
    suspend fun update(item: ItemEntity)

    @Insert
    suspend fun insert(item : ItemEntity)

    @Delete
    suspend fun delete(item: ItemEntity)

    @Query("DELETE FROM items")
    suspend fun deleteAll()

    @Query("SELECT SUM(totalOrder) FROM items")
    fun getTotalCount() : Flow<Int?>

    @Query("SELECT SUM(totalOrder*price) FROM items")
    fun getTotalPrice() : Flow<Double?>


}