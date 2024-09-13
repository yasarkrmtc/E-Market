package com.emarket.data.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDAO {

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<ItemEntity>>

}