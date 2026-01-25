package com.foodics.tables.data.repository


import androidx.room.withTransaction
import com.foodics.core.common.result.ResponseState
import com.foodics.core.database.AppDatabase
import com.foodics.core.database.dao.CategoryDao
import com.foodics.core.database.dao.ProductDao
import com.foodics.core.database.dbCall
import com.foodics.tables.data.mapper.CartSummaryDbToDomainMapper
import com.foodics.tables.data.mapper.CategoryEntityToDomainMapper
import com.foodics.tables.data.mapper.CategoryRemoteToEntityMapper
import com.foodics.tables.data.mapper.MapQuantityProvider
import com.foodics.tables.data.mapper.ProductEntityToDomainMapper
import com.foodics.tables.data.mapper.ProductRemoteToEntityMapper
import com.foodics.tables.data.remote.TablesRemoteDataSource
import com.foodics.tables.domain.model.CartSummary
import com.foodics.tables.domain.model.Category
import com.foodics.tables.domain.model.Product
import com.foodics.tables.domain.repository.TablesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TablesRepositoryImpl(
    private val db: AppDatabase,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val remote: TablesRemoteDataSource,
    private val categoryRemoteToEntity: CategoryRemoteToEntityMapper,
    private val categoryEntityToDomain: CategoryEntityToDomainMapper,
    private val productEntityToDomain: ProductEntityToDomainMapper,
    private val cartSummaryDbToDomain: CartSummaryDbToDomainMapper
) : TablesRepository {

    override fun observeCategories(): Flow<List<Category>> =
        categoryDao.observeCategories()
            .map { list -> list.map { categoryEntityToDomain.map(it) } }

    override fun observeProducts(categoryId: String?, query: String): Flow<List<Product>> =
        productDao.observeProducts(categoryId, query)
            .map { list -> list.map { productEntityToDomain.map(it) } }

    override fun observeCartSummary(): Flow<CartSummary> =
        productDao.observeCartSummary()
            .map { cartSummaryDbToDomain.map(it) }

    override suspend fun addProduct(productId: String) = dbCall { productDao.incrementQty(productId) }

    override suspend fun clearCart(): ResponseState<Unit> = dbCall { productDao.clearCart() }

    override suspend fun syncCategories(): ResponseState<List<Category>> {
        return when (val remoteCategories = remote.getCategories()) {
            is ResponseState.Error -> ResponseState.Error(
                remoteCategories.error,
                remoteCategories.errorBody
            )
            is ResponseState.Success -> {
                val dtos = remoteCategories.data
                val entities = dtos.map { categoryRemoteToEntity.map(it) }
                entities.let {
                    db.withTransaction { categoryDao.upsertAll(entities) }
                }
                val domainCategories = entities.map { categoryEntityToDomain.map(it) }
                ResponseState.Success(domainCategories)
            }
        }
    }

    override suspend fun syncProductsForCategory(categoryId: String): ResponseState<Unit> {
        val state = remote.getProducts(categoryId)
        if (state is ResponseState.Error) return state

        val dtos = (state as ResponseState.Success).data

        // preserve quantity
        val qtyMap = productDao.getAllIdQty().associate { it.id to it.quantity }
        val productMapper = ProductRemoteToEntityMapper(MapQuantityProvider(qtyMap))

        val entities = dtos.map { productMapper.map(it) }

        db.withTransaction { productDao.upsertAll(entities) }
        return ResponseState.Success(Unit)
    }
}