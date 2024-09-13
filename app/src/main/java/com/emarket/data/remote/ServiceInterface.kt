package com.emarket.data.remote

import retrofit2.Call
import retrofit2.http.GET

interface ServiceInterface {

    @GET("products")
    fun fetchProduct(
    ): Call<List<Product>>

}