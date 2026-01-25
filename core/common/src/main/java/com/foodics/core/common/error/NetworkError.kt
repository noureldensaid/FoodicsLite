package com.foodics.core.common.error

import com.foodics.core.common.base.BaseError

enum class NetworkError : BaseError {
    NO_INTERNET_CONNECTION,
    RESOURCE_NOT_FOUND,
    UNAUTHORIZED_ACCESS,
    FORBIDDEN_ACCESS,
    DATA_CONFLICT,
    REQUEST_TIMEOUT_ERROR,
    INTERNAL_SERVER_ERROR,
    CLIENT_API_ERROR,
    RESPONSE_PARSING_ERROR,
    UNKNOWN_ERROR;
}