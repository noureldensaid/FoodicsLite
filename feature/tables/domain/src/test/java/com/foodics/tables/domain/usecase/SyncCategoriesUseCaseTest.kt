package com.foodics.tables.domain.usecase

import com.foodics.core.common.error.NetworkError
import com.foodics.core.common.result.ResponseState
import com.foodics.core.common.result.StatusJsonResponse
import com.foodics.tables.domain.model.Category
import com.foodics.tables.domain.repository.TablesRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SyncCategoriesUseCaseTest {

    private val repo = mockk<TablesRepository>()
    private val useCase = SyncCategoriesUseCase(repo)

    @Test
    fun `returns Success from repo`() = runTest {
        val expected = ResponseState.Success(listOf(Category("c1", "Beef")))
        coEvery { repo.syncCategories() } returns expected

        val actual = useCase()

        coVerify(exactly = 1) { repo.syncCategories() }
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `returns Error from repo`() = runTest {
        val expected = ResponseState.Error(
            error = NetworkError.NO_INTERNET_CONNECTION,
            errorBody = StatusJsonResponse(-1, "offline")
        )
        coEvery { repo.syncCategories() } returns expected

        val actual = useCase()

        coVerify(exactly = 1) { repo.syncCategories() }
        assertThat(actual).isEqualTo(expected)
    }
}