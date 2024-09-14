package com.emarket.domain.repository

import com.emarket.data.remote.Product

interface ServiceRepository {
    suspend fun insertDataBaseItem(item: Product)
}