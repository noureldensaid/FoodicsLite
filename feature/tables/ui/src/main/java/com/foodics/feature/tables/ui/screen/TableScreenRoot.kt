package com.foodics.feature.tables.ui.screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.foodics.core.ui.components.emptyStates.DefaultEmptyState
import com.foodics.core.ui.components.textField.DefaultSearchTextField
import com.foodics.feature.tables.ui.components.CategoryChip
import com.foodics.feature.tables.ui.components.ProductCard
import com.foodics.feature.tables.ui.components.ProductCardLoadingShimmer
import com.foodics.feature.tables.ui.components.TablesTopBar
import com.foodics.feature.tables.ui.components.ViewOrderBar
import com.foodics.feature.tables.ui.model.TablesScreenEvent
import com.foodics.feature.tables.ui.model.TablesScreenState
import com.foodics.tables.domain.model.CartSummary
import com.foodics.tables.domain.model.Category
import com.foodics.tables.domain.model.Product
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch

@Composable
fun TableScreenRoot(
    modifier: Modifier = Modifier,
    state: TablesScreenState,
    onEvent: (TablesScreenEvent) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    var isRefreshing by rememberSaveable { mutableStateOf(false) }

    val pullToRefreshState = rememberPullToRefreshState()

    val lazyRowState = rememberLazyListState()

    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex,
        pageCount = { state.categories.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
        if (state.categories.isNotEmpty()) {
            onEvent(TablesScreenEvent.OnCategorySelected(state.categories[selectedTabIndex].id))
            pagerState.scrollToPage(selectedTabIndex)
            lazyRowState.animateScrollToItem(selectedTabIndex)
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TablesTopBar(isSyncing = state.isSyncing, isOnline = state.isOnline) },
    ) { padding ->
        PullToRefreshBox(
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            indicator = {
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    containerColor = MaterialTheme.colorScheme.background,
                    color = MaterialTheme.colorScheme.primary,
                    state = pullToRefreshState
                )
            },
            onRefresh = {
                isRefreshing = true
                onEvent(TablesScreenEvent.LoadInitialData)
                isRefreshing = false
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start,
        ) {
            DefaultSearchTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                query = state.searchQuery,
                onSearch = { searchQuery ->
                    onEvent(TablesScreenEvent.OnSearchQueryChanged(searchQuery))
                }
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = lazyRowState,
                contentPadding = PaddingValues(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(state.categories, key = { it.id }) { category ->
                    CategoryChip(
                        title = category.name,
                        selected = category.id == state.selectedCategoryId,
                        onClick = {
                            selectedTabIndex = state.categories.indexOf(category)
                            coroutineScope.launch {
                                pagerState.scrollToPage(selectedTabIndex)
                                lazyRowState.animateScrollToItem(selectedTabIndex)
                            }
                            onEvent(TablesScreenEvent.OnCategorySelected(category.id))
                        }
                    )
                }
            }

            if (state.categories.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState,
                        userScrollEnabled = state.searchQuery.isBlank(),
                    ) {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(
                                12.dp,
                                if (state.products.isEmpty()) Alignment.CenterVertically else Alignment.Top
                            ),
                            columns = GridCells.Adaptive(100.dp),
                            contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp)
                        ) {
                            when {
                                state.products.isEmpty() && state.isSyncing -> items(10) {
                                    ProductCardLoadingShimmer(modifier = Modifier.size(150.dp))
                                }

                                state.products.isEmpty() -> item(
                                    span = { GridItemSpan(maxLineSpan) }) {
                                    DefaultEmptyState()
                                }

                                else -> items(
                                    items = state.products,
                                    key = { it.id }
                                ) { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = {
                                            onEvent(TablesScreenEvent.OnProductClicked(product.id))
                                        }
                                    )
                                }
                            }
                        }
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        visible = state.cartSummary.totalQty > 0,
                        enter = slideInHorizontally(),
                        exit = slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { fullWidth -> fullWidth }
                        ),
                    ) {
                        ViewOrderBar(
                            modifier = Modifier
                                .padding(
                                    start = 12.dp,
                                    end = 12.dp,
                                    bottom = 55.dp
                                ),
                            qty = state.cartSummary.totalQty,
                            totalPrice = state.cartSummary.totalPrice,
                            onClick = { onEvent(TablesScreenEvent.OnViewOrderClicked) }
                        )
                    }
                }
            }
            }
        }
    }
}

@Preview(name = "Tables - With Products + Cart")
@Composable
private fun TableScreenRootPreview_WithProductsAndCart() {
    MaterialTheme {
        TableScreenRoot(
            state = TablesScreenState(
                categories = persistentListOf(
                    Category(id = "c1", name = "Coffee"),
                    Category(id = "c2", name = "Desserts"),
                    Category(id = "c3", name = "Sandwiches"),
                ),
                products = persistentListOf(
                    Product(
                        id = "p1",
                        name = "Americano",
                        description = "Single-origin, smooth",
                        image = "",
                        price = 18.0,
                        categoryId = "c1",
                        categoryName = "Coffee",
                        quantity = 0
                    ),
                    Product(
                        id = "p2",
                        name = "Latte",
                        description = "With oat milk",
                        image = "",
                        price = 24.0,
                        categoryId = "c1",
                        categoryName = "Coffee",
                        quantity = 0
                    ),
                    Product(
                        id = "p3",
                        name = "Cheesecake",
                        description = "Classic",
                        image = "",
                        price = 32.0,
                        categoryId = "c2",
                        categoryName = "Desserts",
                        quantity = 0
                    ),
                ),
                selectedCategoryId = "c1",
                searchQuery = "",
                cartSummary = CartSummary(totalQty = 3, totalPrice = 74.0),
                isLoading = false,
                isSyncing = false,
            ),
            onEvent = {}
        )
    }
}

@Preview(name = "Tables - Empty")
@Composable
private fun TableScreenRootPreview_Empty() {
    MaterialTheme {
        TableScreenRoot(
            state = TablesScreenState(
                categories = persistentListOf(
                    Category(id = "c1", name = "Coffee"),
                    Category(id = "c2", name = "Desserts"),
                    Category(id = "c3", name = "Sandwiches"),
                ),
                products = persistentListOf(),
                selectedCategoryId = "c1",
                searchQuery = "",
                cartSummary = CartSummary(totalQty = 0, totalPrice = 0.0),
                isLoading = false,
                isSyncing = false,
            ),
            onEvent = {}
        )
    }
}

@Preview(name = "Tables - Searching (Empty State)")
@Composable
private fun TableScreenRootPreview_SearchEmpty() {
    MaterialTheme {
        TableScreenRoot(
            state = TablesScreenState(
                categories = persistentListOf(
                    Category(id = "c1", name = "Coffee"),
                    Category(id = "c2", name = "Desserts"),
                ),
                products = persistentListOf(),
                selectedCategoryId = "c1",
                searchQuery = "zzzz",
                cartSummary = CartSummary(totalQty = 0, totalPrice = 0.0),
                isLoading = false,
                isSyncing = false,
            ),
            onEvent = {}
        )
    }
}

@Preview(name = "Tables - Loading")
@Composable
private fun TableScreenRootPreview_Loading() {
    MaterialTheme {
        TableScreenRoot(
            state = TablesScreenState(
                categories = persistentListOf(
                    Category(id = "c1", name = "Coffee"),
                    Category(id = "c2", name = "Desserts"),
                ),
                products = persistentListOf(),
                selectedCategoryId = "c1",
                searchQuery = "",
                cartSummary = CartSummary(totalQty = 0, totalPrice = 0.0),
                isLoading = true,
                isSyncing = true,
            ),
            onEvent = {}
        )
    }
}