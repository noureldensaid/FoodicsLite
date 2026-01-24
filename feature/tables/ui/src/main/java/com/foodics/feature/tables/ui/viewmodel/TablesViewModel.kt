package com.foodics.feature.tables.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodics.core.common.result.ResponseState
import com.foodics.core.common.result.StatusJsonResponse
import com.foodics.core.database.DatabaseError
import com.foodics.feature.tables.ui.model.TablesScreenEvent
import com.foodics.feature.tables.ui.model.TablesScreenState
import com.foodics.tables.domain.model.CartSummary
import com.foodics.tables.domain.usecase.AddProductUseCase
import com.foodics.tables.domain.usecase.ClearCartUseCase
import com.foodics.tables.domain.usecase.ObserveCartSummaryUseCase
import com.foodics.tables.domain.usecase.ObserveCategoriesUseCase
import com.foodics.tables.domain.usecase.ObserveProductsUseCase
import com.foodics.tables.domain.usecase.SyncCategoriesUseCase
import com.foodics.tables.domain.usecase.SyncProductsForCategoryUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TablesViewModel(
    private val observeCategoriesUseCase: ObserveCategoriesUseCase,
    private val observeProductsUseCase: ObserveProductsUseCase,
    private val observeCartSummaryUseCase: ObserveCartSummaryUseCase,
    private val syncCategoriesUseCase: SyncCategoriesUseCase,
    private val syncProductsForCategoryUseCase: SyncProductsForCategoryUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _errorFlow = Channel<ResponseState.Error>(Channel.BUFFERED)
    val errorFlow = _errorFlow.receiveAsFlow()

    private var hasInitialDataLoaded = false
    private var didSetDefaultCategory = false
    private var didSyncDefaultCategoryProducts = false

    private val _state = MutableStateFlow(TablesScreenState())
    val state = _state.asStateFlow()
        .onStart {
            if (!hasInitialDataLoaded) {
                onEvent(TablesScreenEvent.LoadInitialData)
                hasInitialDataLoaded = true
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    fun onEvent(event: TablesScreenEvent) {
        when (event) {
            TablesScreenEvent.LoadInitialData -> loadInitialData()
            is TablesScreenEvent.OnCategorySelected -> onCategorySelected(event.categoryId)
            is TablesScreenEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.query)
            is TablesScreenEvent.OnProductClicked -> onProductClicked(event.productId)
            TablesScreenEvent.OnViewOrderClicked -> onViewOrderClicked()
        }
    }

    private fun loadInitialData() {
        observeCategories()
        observeCartSummary()
        observeProducts()

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, isSyncing = true) }

            when (val response = syncCategoriesUseCase()) {
                is ResponseState.Success -> {
                    val categories = response.data
                    _state.update { it.copy(categories = categories.toPersistentList()) }
                }

                is ResponseState.Error -> _errorFlow.send(response)
            }

            _state.update { it.copy(isLoading = false, isSyncing = false) }
        }
    }

    private fun observeCategories() {
        viewModelScope.launch {
            observeCategoriesUseCase().collect { list ->

                _state.update { current ->
                    val selected = when {
                        current.selectedCategoryId != null -> current.selectedCategoryId
                        !didSetDefaultCategory && list.isNotEmpty() -> {
                            didSetDefaultCategory = true
                            list.first().id
                        }
                        else -> current.selectedCategoryId
                    }

                    current.copy(
                        categories = list.toPersistentList(),
                        selectedCategoryId = selected
                    )
                }

                val selectedId = _state.value.selectedCategoryId
                if (!didSyncDefaultCategoryProducts && selectedId != null && list.isNotEmpty()) {
                    didSyncDefaultCategoryProducts = true
                    onCategorySelected(selectedId)
                }
            }
        }
    }

    private fun observeCartSummary() {
        viewModelScope.launch {
            observeCartSummaryUseCase().collect { summary ->
                _state.update { it.copy(cartSummary = summary) }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeProducts() {
        viewModelScope.launch {
            state
                .map { it.selectedCategoryId to it.searchQuery }
                .distinctUntilChanged()
                .flatMapLatest { (categoryId, query) ->
                    observeProductsUseCase(categoryId, query)
                }
                .collect { list ->
                    _state.update { it.copy(products = list.toPersistentList()) }
                }
        }
    }

    private fun onCategorySelected(categoryId: String) {
        viewModelScope.launch {
            if (_state.value.selectedCategoryId == categoryId && _state.value.products.isNotEmpty()) {
                _state.update { it.copy(selectedCategoryId = categoryId) }
                return@launch
            }

            _state.update {
                it.copy(
                    selectedCategoryId = categoryId,
                    isSyncing = true,
                    searchQuery = ""
                )
            }

            when (val response = syncProductsForCategoryUseCase(categoryId)) {
                is ResponseState.Success -> Unit
                is ResponseState.Error -> _errorFlow.send(response)
            }
            _state.update { it.copy(isSyncing = false) }
        }
    }

    private fun onSearchQueryChanged(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    private fun onProductClicked(productId: String) {
        viewModelScope.launch {
            runCatching { addProductUseCase(productId) }
                .onFailure {
                    _errorFlow.send(
                        ResponseState.Error(
                            error = DatabaseError.QUERY_FAILED,
                            errorBody = StatusJsonResponse(
                                message = it.message,
                                code = -1
                            ),
                        )
                    )
                }
        }
    }


    private fun onViewOrderClicked() {
        viewModelScope.launch {
            runCatching { clearCartUseCase() }
                .onFailure {
                    _errorFlow.send(
                        ResponseState.Error(
                            error = DatabaseError.DELETE_FAILED,
                            errorBody = StatusJsonResponse(
                                message = it.message,
                                code = -1
                            ),
                        )
                    )
                }
            _state.update {
                it.copy(
                    cartSummary = CartSummary(totalQty = 0, totalPrice = 0.0),
                )
            }
        }
    }
}