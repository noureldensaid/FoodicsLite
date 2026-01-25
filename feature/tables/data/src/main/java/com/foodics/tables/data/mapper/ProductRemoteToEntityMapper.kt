package com.foodics.tables.data.mapper

import com.foodics.core.common.base.BaseMapper
import com.foodics.core.database.entity.ProductEntity
import com.foodics.tables.data.remote.model.ProductRemoteDto

class ProductRemoteToEntityMapper(
    private val quantityProvider: QuantityProvider
) : BaseMapper<ProductRemoteDto, ProductEntity> {

    override fun map(from: ProductRemoteDto): ProductEntity {
        return ProductEntity(
            id = from.id,
            name = from.name,
            description = from.description,
            image = from.image,
            price = PriceParser.parse(from.price),
            categoryId = from.categoryId,
            categoryName = from.categoryName,
            quantity = quantityProvider.getQuantity(from.id)
        )
    }
}


interface QuantityProvider {
    fun getQuantity(productId: String): Int
}

object PriceParser {
    fun parse(value: String): Double =
        value.trim().replace(",", ".").toDoubleOrNull() ?: 0.0
}

class MapQuantityProvider(
    private val quantities: Map<String, Int>
) : QuantityProvider {
    override fun getQuantity(productId: String): Int = quantities[productId] ?: 0
}