package com.emarket.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ItemEntity::class], version = 1)
abstract class CartDataBase : RoomDatabase() {
    abstract fun productsDao(): CartDAO
}