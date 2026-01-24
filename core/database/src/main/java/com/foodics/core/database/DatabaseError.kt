package com.foodics.core.database

import com.foodics.core.common.base.BaseError

enum class DatabaseError : BaseError {
    QUERY_FAILED,
    DELETE_FAILED
}