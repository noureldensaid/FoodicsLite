package com.foodics.tables.domain.usecase

import app.cash.turbine.test
import com.foodics.tables.domain.model.Category
import com.foodics.tables.domain.repository.TablesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveCategoriesUseCaseTest {

    private val repo = mockk<TablesRepository>()
    private val useCase = ObserveCategoriesUseCase(repo)

    @Test
    fun `emits values from repo`() = runTest {
        val first = listOf(Category(id = "c1", name = "Beef"))
        val second = listOf(Category(id = "c2", name = "Chicken"))

        every { repo.observeCategories() } returns flowOf(first, second)

        useCase().test {
            assertThat(awaitItem()).isEqualTo(first)
            assertThat(awaitItem()).isEqualTo(second)
            awaitComplete()
        }
    }
}