package com.foodics.tables.data.mapper

import com.foodics.core.common.base.BaseMapper
import com.foodics.core.database.entity.CategoryEntity
import com.foodics.tables.domain.model.Category

class CategoryEntityToDomainMapper : BaseMapper<CategoryEntity, Category> {
    override fun map(from: CategoryEntity): Category {
        return Category(
            id = from.id,
            name = from.name
        )
    }
}