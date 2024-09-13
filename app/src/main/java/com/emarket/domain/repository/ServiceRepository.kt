package com.emarket.domain.repository

import com.emarket.core.Response
import com.emarket.data.remote.Product
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {
    suspend fun getProducts(): Flow<Response<List<Product>>>
}