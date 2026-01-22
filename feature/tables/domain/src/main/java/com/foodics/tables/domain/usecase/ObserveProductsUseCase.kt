package com.foodics.tables.domain.usecase

import com.foodics.tables.domain.repository.TablesRepository


class ObserveProductsUseCase(
    private val repo: TablesRepository
) {
    operator fun invoke(categoryId: String?, query: String) = repo.observeProducts(categoryId, query)
}