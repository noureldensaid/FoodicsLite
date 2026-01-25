package com.foodics.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices = [Index("categoryId")]
)
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val image: String,
    val price: Double,
    val categoryId: String,
    val categoryName: String,
    val quantity: Int
)