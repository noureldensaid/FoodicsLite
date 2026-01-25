package com.foodics.tables.data.remote

import com.foodics.core.common.result.ResponseState
import com.foodics.core.network.safeApiCall
import com.foodics.tables.data.remote.model.CategoryRemoteDto
import com.foodics.tables.data.remote.model.ProductRemoteDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TablesRemoteDataSourceImpl(
    private val client: HttpClient
) : TablesRemoteDataSource {

    override suspend fun getCategories(): ResponseState<List<CategoryRemoteDto>> =
        safeApiCall { client.get("categories.json") }

    override suspend fun getProducts(categoryId: String): ResponseState<List<ProductRemoteDto>> =
        safeApiCall {
            client.get("products.json") {
                parameter("category_id", categoryId)
            }
        }
}