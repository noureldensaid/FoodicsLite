package com.foodics.tables.data.mapper

import com.foodics.core.common.base.BaseMapper
import com.foodics.core.database.entity.ProductEntity
import com.foodics.tables.domain.model.Product

class ProductEntityToDomainMapper : BaseMapper<ProductEntity, Product> {
    override fun map(from: ProductEntity): Product {
        return Product(
            id = from.id,
            name = from.name,
            description = from.description,
            image = from.image,
            price = from.price,
            categoryId = from.categoryId,
            categoryName = from.categoryName,
            quantity = from.quantity
        )
    }
}