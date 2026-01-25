package com.foodics.tables.domain.usecase

import app.cash.turbine.test
import com.foodics.tables.domain.model.CartSummary
import com.foodics.tables.domain.repository.TablesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveCartSummaryUseCaseTest {

    private val repo = mockk<TablesRepository>()
    private val useCase = ObserveCartSummaryUseCase(repo)

    @Test
    fun `emits cart summary from repo`() = runTest {
        val emitted = CartSummary(totalQty = 3, totalPrice = 18.0)
        every { repo.observeCartSummary() } returns flowOf(emitted)

        useCase().test {
            assertThat(awaitItem()).isEqualTo(emitted)
            awaitComplete()
        }
    }
}