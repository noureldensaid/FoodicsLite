package com.foodics.tables.domain.usecase

import com.foodics.tables.domain.repository.TablesRepository

class SyncProductsForCategoryUseCase(
    private val repo: TablesRepository
) {
    suspend operator fun invoke(categoryId: String) = repo.syncProductsForCategory(categoryId)
}