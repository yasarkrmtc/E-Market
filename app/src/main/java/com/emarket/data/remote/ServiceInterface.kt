package com.emarket.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceInterface {

    @GET("products")
    suspend fun fetchProducts(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("brand") brand: String,
        @Query("model") model: String,
        @Query("sortBy") sortedBy : String
    ): Response<List<Product>>

}
