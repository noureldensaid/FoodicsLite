package com.foodics.tables.domain.repository

import com.foodics.core.common.result.ResponseState
import com.foodics.tables.domain.model.CartSummary
import com.foodics.tables.domain.model.Category
import com.foodics.tables.domain.model.Product
import kotlinx.coroutines.flow.Flow


interface TablesRepository {

    fun observeCategories(): Flow<List<Category>>

    fun observeProducts(
        categoryId: String?,
        query: String
    ): Flow<List<Product>>

    fun observeCartSummary(): Flow<CartSummary>

    suspend fun syncCategories(): ResponseState<List<Category>>
    suspend fun syncProductsForCategory(categoryId: String): ResponseState<Unit>

    suspend fun addProduct(productId: String)
    suspend fun clearCart()

}