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

class AddProductUseCaseTest {

    private val repo = mockk<TablesRepository>()
    private val useCase = AddProductUseCase(repo)

    @Test
    fun `invoke forwards productId and returns Success`() = runTest {
        val productId = "p1"
        val expected = ResponseState.Success(Unit)

        coEvery { repo.addProduct(productId) } returns expected

        val actual = useCase(productId)

        coVerify(exactly = 1) { repo.addProduct(productId) }
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `invoke forwards productId and returns Error`() = runTest {
        val productId = "p1"
        val expected = ResponseState.Error(
            error = DatabaseError.QUERY_FAILED,
            errorBody = StatusJsonResponse(message = "boom", code = -1)
        )

        coEvery { repo.addProduct(productId) } returns expected

        val actual = useCase(productId)

        coVerify(exactly = 1) { repo.addProduct(productId) }
        assertThat(actual).isEqualTo(expected)
    }
}