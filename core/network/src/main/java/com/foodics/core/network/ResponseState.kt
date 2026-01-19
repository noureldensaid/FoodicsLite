package com.foodics.core.network

import com.foodics.core.common.base.BaseError

sealed interface ResponseState<out D> {
    data class Success<out D>(val data: D) : ResponseState<D>
    data class Error(val error: BaseError, val errorBody: StatusJsonResponse?) : ResponseState<Nothing>
}