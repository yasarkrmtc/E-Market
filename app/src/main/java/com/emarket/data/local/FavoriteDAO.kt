package com.emarket.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDAO {
    @Query("SELECT * FROM favorites")
    fun getAllItems(): Flow<List<FavoriteItemEntity>>

    @Query("SELECT * FROM favorites WHERE id = :itemId")
    suspend fun getItemById(itemId: String): FavoriteItemEntity

    @Update
    suspend fun update(item: FavoriteItemEntity)

    @Insert
    suspend fun insert(item : FavoriteItemEntity)

    @Delete
    suspend fun delete(item: FavoriteItemEntity)

    @Query("DELETE FROM favorites")
    suspend fun deleteAll()
}