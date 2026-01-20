package com.foodics.tables.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductRemoteDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String? = null,
    @SerialName("image") val image: String,
    @SerialName("price") val price: String,
    @SerialName("category_id") val categoryId: String,
    @SerialName("category_name") val categoryName: String
)