package com.foodics.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatusJsonResponse(
    @SerialName("code")
    val code: Int?,
    @SerialName("message")
    val message: String?
)