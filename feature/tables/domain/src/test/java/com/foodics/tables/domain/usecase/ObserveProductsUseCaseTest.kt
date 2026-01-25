package com.foodics.tables.domain.usecase

import app.cash.turbine.test
import com.foodics.tables.domain.model.Product
import com.foodics.tables.domain.repository.TablesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveProductsUseCaseTest {

    private val repo = mockk<TablesRepository>()
    private val useCase = ObserveProductsUseCase(repo)

    @Test
    fun `forwards categoryId and query and emits values`() = runTest {
        val categoryId = "c1"
        val query = "bur"
        val emitted = listOf(
            Product(
                id = "p1",
                name = "Burger",
                description = "Meal",
                image = "img",
                price = 6.0,
                categoryId = "c1",
                categoryName = "Beef",
                quantity = 0
            )
        )

        every { repo.observeProducts(categoryId, query) } returns flowOf(emitted)

        useCase(categoryId, query).test {
            assertThat(awaitItem()).isEqualTo(emitted)
            awaitComplete()
        }
    }
}