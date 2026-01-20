package com.foodics.tables.domain.usecase

import com.foodics.tables.domain.repository.TablesRepository

class GetOrderedProductsUseCase(
    private val repo: TablesRepository
) {
    suspend operator fun invoke() = repo.getOrderedProducts()
}