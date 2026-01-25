package com.foodics.core.database

import com.foodics.core.common.error.DatabaseError
import com.foodics.core.common.result.ResponseState
import com.foodics.core.common.result.StatusJsonResponse
import kotlin.coroutines.cancellation.CancellationException

inline fun <T> dbCall(block: () -> T): ResponseState<T> =
    try {
        ResponseState.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        ResponseState.Error(
            error = DatabaseError.QUERY_FAILED,
            errorBody = StatusJsonResponse(message = e.message, code = -1)
        )
    }