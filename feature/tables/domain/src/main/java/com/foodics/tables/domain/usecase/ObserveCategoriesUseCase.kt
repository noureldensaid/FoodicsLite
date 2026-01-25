package com.foodics.tables.domain.usecase

import com.foodics.tables.domain.repository.TablesRepository

class ObserveCategoriesUseCase(
    private val repo: TablesRepository
) {
    operator fun invoke() = repo.observeCategories()
}