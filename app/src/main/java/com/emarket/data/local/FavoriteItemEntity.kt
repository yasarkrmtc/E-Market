package com.emarket.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteItemEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val createdAt: String,
    val name: String,
    val image: String,
    val price: String,
    val description: String,
    val model: String,
    val brand: String,
    var isFavorite :Boolean = false
)