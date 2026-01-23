package com.foodics.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.foodics.core.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductDao {

    // Products list with search + category filtering
    @Query("""
    SELECT * FROM products
    WHERE 
      (TRIM(:query) != '' AND LOWER(name) LIKE '%' || LOWER(TRIM(:query)) || '%')
      OR
      (TRIM(:query) = '' AND (:categoryId IS NULL OR categoryId = :categoryId))
    """)
    fun observeProducts(categoryId: String?, query: String): Flow<List<ProductEntity>>
    // For sync merge to preserve quantity
    data class IdQty(val id: String, val quantity: Int)

    @Query("SELECT id, quantity FROM products")
    suspend fun getAllIdQty(): List<IdQty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ProductEntity>)

    // Cart behavior
    @Query("UPDATE products SET quantity = quantity + 1 WHERE id = :productId")
    suspend fun incrementQty(productId: String)

    @Query("UPDATE products SET quantity = 0 WHERE quantity > 0")
    suspend fun clearCart()

    // Cart summary derived only from products table
    data class CartSummaryDb(val totalQty: Int, val totalPrice: Double)

    @Query("""
        SELECT 
            COALESCE(SUM(quantity), 0) AS totalQty,
            COALESCE(SUM(quantity * price), 0.0) AS totalPrice
        FROM products
        WHERE quantity > 0
    """)
    fun observeCartSummary(): Flow<CartSummaryDb>

    @Query("SELECT * FROM products WHERE quantity > 0 ORDER BY name ASC")
    suspend fun getOrderedProducts(): List<ProductEntity>

}
