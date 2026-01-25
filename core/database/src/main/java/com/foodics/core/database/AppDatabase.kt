package com.foodics.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.foodics.core.database.dao.CategoryDao
import com.foodics.core.database.dao.ProductDao
import com.foodics.core.database.entity.CategoryEntity
import com.foodics.core.database.entity.ProductEntity


@Database(
    entities = [
        CategoryEntity::class,
        ProductEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
}