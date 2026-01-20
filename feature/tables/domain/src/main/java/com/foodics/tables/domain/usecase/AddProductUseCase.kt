package com.foodics.tables.domain.usecase

import com.foodics.tables.domain.repository.TablesRepository


class AddProductUseCase(
    private val repo: TablesRepository
) {
    suspend operator fun invoke(productId: String) = repo.addProduct(productId)
}