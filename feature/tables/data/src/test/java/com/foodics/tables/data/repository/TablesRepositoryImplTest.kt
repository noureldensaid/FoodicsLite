package com.foodics.tables.data.repository

import app.cash.turbine.test
import com.foodics.core.common.error.NetworkError
import com.foodics.core.common.result.ResponseState
import com.foodics.core.common.result.StatusJsonResponse
import com.foodics.core.database.AppDatabase
import com.foodics.core.database.dao.CategoryDao
import com.foodics.core.database.dao.ProductDao
import com.foodics.core.database.entity.CategoryEntity
import com.foodics.core.database.entity.ProductEntity
import com.foodics.tables.data.mapper.CartSummaryDbToDomainMapper
import com.foodics.tables.data.mapper.CategoryEntityToDomainMapper
import com.foodics.tables.data.mapper.CategoryRemoteToEntityMapper
import com.foodics.tables.data.mapper.ProductEntityToDomainMapper
import com.foodics.tables.data.remote.TablesRemoteDataSource
import com.foodics.tables.data.remote.model.ProductRemoteDto
import com.foodics.tables.domain.model.CartSummary
import com.foodics.tables.domain.model.Category
import com.foodics.tables.domain.model.Product
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.concurrent.Executor

class TablesRepositoryImplTest {

    private val db = mockk<AppDatabase>(relaxed = true)
    private val categoryDao = mockk<CategoryDao>(relaxed = true)
    private val productDao = mockk<ProductDao>(relaxed = true)
    private val remote = mockk<TablesRemoteDataSource>(relaxed = true)

    private val categoryRemoteToEntity = mockk<CategoryRemoteToEntityMapper>(relaxed = true)
    private val categoryEntityToDomain = mockk<CategoryEntityToDomainMapper>(relaxed = true)
    private val productEntityToDomain = mockk<ProductEntityToDomainMapper>(relaxed = true)
    private val cartSummaryDbToDomain = mockk<CartSummaryDbToDomainMapper>(relaxed = true)

    private lateinit var repo: TablesRepositoryImpl


    @Before
    fun setUp() {

        every { db.transactionExecutor } returns Executor { runnable -> runnable.run() }

        repo = TablesRepositoryImpl(
            db = db,
            categoryDao = categoryDao,
            productDao = productDao,
            remote = remote,
            categoryRemoteToEntity = categoryRemoteToEntity,
            categoryEntityToDomain = categoryEntityToDomain,
            productEntityToDomain = productEntityToDomain,
            cartSummaryDbToDomain = cartSummaryDbToDomain
        )
    }

    @Test
    fun `observeCategories maps entities to domain`() = runTest {
        val e1 = mockk<CategoryEntity>()
        val e2 = mockk<CategoryEntity>()

        val d1 = Category(id = "c1", name = "Beef")
        val d2 = Category(id = "c2", name = "Chicken")

        every { categoryDao.observeCategories() } returns flowOf(listOf(e1, e2))
        every { categoryEntityToDomain.map(e1) } returns d1
        every { categoryEntityToDomain.map(e2) } returns d2

        repo.observeCategories().test {
            assertThat(awaitItem()).isEqualTo(listOf(d1, d2))
            awaitComplete()
        }
    }

    @Test
    fun `observeProducts maps entities to domain`() = runTest {
        val categoryId = "c1"
        val query = "bur"

        val pEntity = mockk<ProductEntity>()
        val pDomain = mockk<Product>(relaxed = true)

        every { productDao.observeProducts(categoryId, query) } returns flowOf(listOf(pEntity))
        every { productEntityToDomain.map(pEntity) } returns pDomain

        repo.observeProducts(categoryId, query).test {
            assertThat(awaitItem()).isEqualTo(listOf(pDomain))
            awaitComplete()
        }
    }

    @Test
    fun `observeCartSummary maps db model to domain`() = runTest {
        val dbModel = mockk<ProductDao.CartSummaryDb>()
        val domain = CartSummary(totalQty = 2, totalPrice = 12.0)

        every { productDao.observeCartSummary() } returns flowOf(dbModel)
        every { cartSummaryDbToDomain.map(dbModel) } returns domain

        repo.observeCartSummary().test {
            assertThat(awaitItem()).isEqualTo(domain)
            awaitComplete()
        }
    }

    @Test
    fun `addProduct calls incrementQty and returns Success`() = runTest {
        val productId = "p1"
        coEvery { productDao.incrementQty(productId) } returns Unit

        val result = repo.addProduct(productId)

        coVerify(exactly = 1) { productDao.incrementQty(productId) }
        assertThat(result).isEqualTo(ResponseState.Success(Unit))
    }

    @Test
    fun `clearCart calls dao and returns Success`() = runTest {
        coEvery { productDao.clearCart() } returns Unit

        val result = repo.clearCart()

        coVerify(exactly = 1) { productDao.clearCart() }
        assertThat(result).isEqualTo(ResponseState.Success(Unit))
    }

    @Test
    fun `syncCategories returns remote Error and does not upsert`() = runTest {
        val remoteError = ResponseState.Error(
            error = NetworkError.FORBIDDEN_ACCESS,
            errorBody = StatusJsonResponse(message = "fail", code = -1)
        )

        coEvery { remote.getCategories() } returns remoteError

        val result = repo.syncCategories()

        assertThat(result).isEqualTo(remoteError)
        coVerify(exactly = 0) { categoryDao.upsertAll(any()) }
    }

    @Test
    fun `syncProductsForCategory success preserves qtyMap and upserts`() = runTest {
        val categoryId = "c1"

        val dto = mockk<ProductRemoteDto>(relaxed = true)
        every { dto.id } returns "p1"
        every { dto.name } returns "Burger"
        every { dto.description } returns "Meal"
        every { dto.image } returns "img"
        every { dto.price } returns "6.0"
        every { dto.categoryId } returns "c1"
        every { dto.categoryName } returns "Beef"

        coEvery { remote.getProducts(categoryId) } returns ResponseState.Success(listOf(dto))

        coEvery { productDao.getAllIdQty() } returns listOf(
            ProductDao.IdQty(
                id = "p1",
                quantity = 3
            )
        )
        coEvery { productDao.upsertAll(any()) } returns Unit

        val result = repo.syncProductsForCategory(categoryId)

        coVerify(exactly = 1) { productDao.getAllIdQty() }
        coVerify(exactly = 1) { productDao.upsertAll(any()) }
        assertThat(result).isEqualTo(ResponseState.Success(Unit))
    }
}