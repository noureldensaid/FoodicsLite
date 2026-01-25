package com.foodics.core.common.error

import com.foodics.core.common.base.BaseError

enum class DatabaseError : BaseError {
    QUERY_FAILED,
    DELETE_FAILED
}