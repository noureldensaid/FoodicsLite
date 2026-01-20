package com.foodics.tables.data.mapper

import com.foodics.core.common.base.BaseMapper
import com.foodics.core.database.entity.CategoryEntity
import com.foodics.tables.data.remote.model.CategoryRemoteDto

class CategoryRemoteToEntityMapper : BaseMapper<CategoryRemoteDto, CategoryEntity> {
    override fun map(from: CategoryRemoteDto): CategoryEntity {
        return CategoryEntity(
            id = from.id,
            name = from.name
        )
    }
}