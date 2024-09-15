package com.emarket.domain.repository

import com.emarket.data.local.ItemEntity
import com.emarket.data.remote.Product
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    suspend fun getTotalPrice(): Flow<Double?>
    suspend fun insertDataBaseItem(item: Product)
    suspend fun getLocalItems(): Flow<List<ItemEntity>>
    suspend fun getLocalItem(id:String): ItemEntity?
    suspend fun deleteAllItems()
    suspend fun updateFavoriteUseCase(item: Product)
    suspend fun getItemsCount():Flow<Int>
}