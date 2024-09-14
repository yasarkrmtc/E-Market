package com.emarket.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceInterface {

    @GET("products")
    suspend fun fetchProducts(@Query("page") page: Int,): List<Product>

}
