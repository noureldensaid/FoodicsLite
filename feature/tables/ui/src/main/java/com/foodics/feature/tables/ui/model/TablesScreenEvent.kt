package com.foodics.feature.tables.ui.model

sealed interface TablesScreenEvent {
    data object LoadInitialData : TablesScreenEvent
    data class OnCategorySelected(val categoryId: String) : TablesScreenEvent
    data class OnSearchQueryChanged(val query: String) : TablesScreenEvent
    data class OnProductClicked(val productId: String) : TablesScreenEvent
    data class UpdateNetworkState(val isOnline: Boolean) : TablesScreenEvent
    data object OnViewOrderClicked : TablesScreenEvent
}