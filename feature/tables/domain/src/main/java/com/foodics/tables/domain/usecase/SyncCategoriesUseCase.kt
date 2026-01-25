package com.foodics.tables.domain.usecase

import com.foodics.tables.domain.repository.TablesRepository


class SyncCategoriesUseCase(
    private val repo: TablesRepository
) {
    suspend operator fun invoke() = repo.syncCategories()
}