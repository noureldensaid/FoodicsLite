package com.foodics.tables.domain.usecase

import com.foodics.core.common.error.DatabaseError
import com.foodics.core.common.result.ResponseState
import com.foodics.core.common.result.StatusJsonResponse
import com.foodics.tables.domain.repository.TablesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ClearCartUseCaseTest {

    private val repo = mockk<TablesRepository>()
    private val useCase = ClearCartUseCase(repo)

    @Test
    fun `invoke returns Success`() = runTest {
        val expected = ResponseState.Success(Unit)
        coEvery { repo.clearCart() } returns expected

        val actual = useCase()

        coVerify(exactly = 1) { repo.clearCart() }
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke returns Error`() = runTest {
        val expected = ResponseState.Error(
            error = DatabaseError.QUERY_FAILED,
            errorBody = StatusJsonResponse(message = "fail", code = -1)
        )
        coEvery { repo.clearCart() } returns expected

        val actual = useCase()

        coVerify(exactly = 1) { repo.clearCart() }
        assertThat(actual).isEqualTo(expected)
    }
}