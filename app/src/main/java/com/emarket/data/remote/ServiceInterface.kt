package com.emarket.data.remote

import retrofit2.Call
import retrofit2.http.GET
interface ServiceInterface {

    @GET("products")
    suspend fun fetchProducts(): List<Product>

}
