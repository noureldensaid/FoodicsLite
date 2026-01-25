package com.foodics.tables.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String?,
    val image: String,
    val price: Double,
    val categoryId: String,
    val categoryName: String,
    val quantity: Int
)