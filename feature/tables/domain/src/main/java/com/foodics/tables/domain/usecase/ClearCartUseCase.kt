package com.foodics.tables.domain.usecase

import com.foodics.tables.domain.repository.TablesRepository

class ClearCartUseCase(
    private val repo: TablesRepository
) {
    suspend operator fun invoke() = repo.clearCart()
}