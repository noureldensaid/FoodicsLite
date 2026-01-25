package com.foodics.core.network

import com.foodics.core.common.error.NetworkError
import com.foodics.core.common.result.ResponseState
import com.foodics.core.common.result.StatusJsonResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.JsonConvertException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import org.json.JSONObject
import timber.log.Timber
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> safeApiCall(crossinline apiCall: suspend () -> HttpResponse): ResponseState<T> {
    return runCatching {
        currentCoroutineContext().ensureActive()
        val response = apiCall.invoke()
        if (response.status.value in 400..599) {
            Timber.tag("safeApiCall").d(response.status.value.toString())
            throw ResponseException(response, "HTTP ${response.status.value}")
        }
        val responseBody: T = response.body()
        ResponseState.Success(responseBody)
    }.getOrElse { e ->
        Timber.tag("safeApiCall").d(e, "Error during API call: $e ${e.message}")
        when (e) {
            is CancellationException -> throw e
            is ResponseException -> handleHttpException(e)
            is UnknownHostException -> ResponseState.Error(NetworkError.NO_INTERNET_CONNECTION, null)
            is IOException -> ResponseState.Error(NetworkError.NO_INTERNET_CONNECTION, null)
            is JsonConvertException -> ResponseState.Error(NetworkError.RESPONSE_PARSING_ERROR, StatusJsonResponse(-1, e.message))
            else -> ResponseState.Error(
                NetworkError.UNKNOWN_ERROR,
                StatusJsonResponse(-1, e.message)
            )
        }
    }
}

/**
 * Handles HTTP-specific exceptions, including API error responses.
 */
suspend fun handleHttpException(e: ResponseException): ResponseState.Error {
    val statusCode = e.response.status.value
    val errorBody = e.response.bodyAsText()
    val parsedError = parseErrorMessage(statusCode, errorBody)

    return when (statusCode) {
        401 -> ResponseState.Error(NetworkError.UNAUTHORIZED_ACCESS, parsedError)
        403 -> ResponseState.Error(NetworkError.FORBIDDEN_ACCESS, parsedError)
        404 -> ResponseState.Error(NetworkError.RESOURCE_NOT_FOUND, parsedError)
        408 -> ResponseState.Error(NetworkError.REQUEST_TIMEOUT_ERROR, parsedError)
        409 -> ResponseState.Error(NetworkError.DATA_CONFLICT, parsedError)
        in 500..599 -> ResponseState.Error(NetworkError.INTERNAL_SERVER_ERROR, parsedError)
        else -> ResponseState.Error(NetworkError.CLIENT_API_ERROR, parsedError)
    }
}

/**
 * Parses API error messages into structured responses.
 */
fun parseErrorMessage(statusCode: Int, errorBody: String?): StatusJsonResponse {
    if (errorBody.isNullOrBlank()) return StatusJsonResponse(statusCode, null)

    return runCatching {
        val json = JSONObject(errorBody)

        // 1) flat: { "error": "Invalid API Key" } or { "message": "..." }
        val flatMsg = json.optString("error").takeIf { it.isNotBlank() }
            ?: json.optString("message").takeIf { it.isNotBlank() }

        // 2) nested: { "status": { "code": 123, "error": "..." } }
        val statusObj = json.optJSONObject("status")
        val nestedCode = statusObj?.optInt("code")?.takeIf { it != 0 }
        val nestedMsg = statusObj?.optString("error")?.takeIf { it.isNotBlank() }
            ?: statusObj?.optString("message")?.takeIf { it.isNotBlank() }

        StatusJsonResponse(
            code = nestedCode ?: statusCode,
            message = nestedMsg ?: flatMsg ?: errorBody
        )
    }.getOrElse { e ->
        Timber.tag("safeApiCall").e(e, "Error parsing error message")
        StatusJsonResponse(statusCode, errorBody)
    }
}