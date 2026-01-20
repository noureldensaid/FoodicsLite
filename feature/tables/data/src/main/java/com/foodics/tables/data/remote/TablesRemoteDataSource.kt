package com.foodics.tables.data.remote

import com.foodics.core.common.result.ResponseState
import com.foodics.tables.data.remote.model.CategoryRemoteDto
import com.foodics.tables.data.remote.model.ProductRemoteDto

interface TablesRemoteDataSource {
    suspend fun getCategories(): ResponseState<List<CategoryRemoteDto>>
    suspend fun getProducts(categoryId: String): ResponseState<List<ProductRemoteDto>>
}