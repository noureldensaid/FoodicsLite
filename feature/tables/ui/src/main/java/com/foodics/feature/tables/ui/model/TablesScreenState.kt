package com.foodics.feature.tables.ui.model


import androidx.compose.runtime.Stable
import com.foodics.tables.domain.model.CartSummary
import com.foodics.tables.domain.model.Category
import com.foodics.tables.domain.model.Product
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class TablesScreenState(
    val categories: PersistentList<Category> = persistentListOf(),
    val products: PersistentList<Product> = persistentListOf(),

    val selectedCategoryId: String? = null,
    val searchQuery: String = "",

    val cartSummary: CartSummary = CartSummary(totalQty = 0, totalPrice = 0.0),

    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
)