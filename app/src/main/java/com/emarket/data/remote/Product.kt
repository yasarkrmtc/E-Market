package com.emarket.data.remote

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("price") val price: String,
    @SerializedName("description") val description: String,
    @SerializedName("model") val model: String,
    @SerializedName("brand") val brand: String
)
