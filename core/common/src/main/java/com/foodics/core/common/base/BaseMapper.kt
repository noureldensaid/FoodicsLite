package com.foodics.core.common.base

interface BaseMapper<F, T> {
    fun map(from: F): T
}