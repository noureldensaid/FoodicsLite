package com.foodics.tables.data.mapper

import com.foodics.core.common.base.BaseMapper
import com.foodics.core.database.dao.ProductDao
import com.foodics.tables.domain.model.CartSummary

class CartSummaryDbToDomainMapper : BaseMapper<ProductDao.CartSummaryDb, CartSummary> {
    override fun map(from: ProductDao.CartSummaryDb): CartSummary {
        return CartSummary(
            totalQty = from.totalQty,
            totalPrice = from.totalPrice
        )
    }
}